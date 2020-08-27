-- ------------------------------------
-- 开启 mysql event scheduler
-- ------------------------------------
-- 方式1：mysql命令开启，重启失效
SET GLOBAL event_scheduler = 1;

-- 方式2：修改/etc/my.cnf配置，长期有效
-- [mysqld]部分加
-- event_scheduler=ON

-- ------------------------------------
-- 表：设备追溯配置
-- ------------------------------------
CREATE TABLE IF NOT EXISTS `map_equipment_config` (
  `id` varchar(32) NOT NULL,
  `eqp_id` varchar(32) NOT NULL COMMENT '设备ID',
  `down_eqp_id` varchar(32) DEFAULT NULL COMMENT '下游设备ID',
  `eqp_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '设备类型(伪类型),0：普通，1：开始，2：报告，4：贴合，8：打码',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人ID',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改日期',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '记录删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQ_eqp_id` (`eqp_id`),
  KEY `KEY_down_eqp_id` (`down_eqp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备追溯配置';

-- ------------------------------------
-- 表：追溯变量表
-- ------------------------------------
CREATE TABLE IF NOT EXISTS `map_variable` (
  `id` varchar(32) NOT NULL COMMENT '变量ID',
  `val` varchar(1024) DEFAULT NULL COMMENT '变量值',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='追溯变量';

INSERT INTO map_variable(id,val) values('last_conn_id',NULL),('last_move_id',NULL),('partition_val','50000');

-- ------------------------------------
-- 表：任务历史记录
-- ------------------------------------
CREATE TABLE IF NOT EXISTS `map_tray_chip_log` (
  `id` varchar(32) NOT NULL COMMENT '记录ID',
  `begin_time` timestamp NULL COMMENT '执行开始时间',
  `end_time` timestamp NULL COMMENT '执行结束时间',
  `begin_no` bigint DEFAULT NULL COMMENT '执行开始ID',
  `end_no` bigint DEFAULT NULL COMMENT '执行结束ID',
  `proc_total` bigint DEFAULT 0 COMMENT '总数量',
  `proc_suc` bigint DEFAULT 0 COMMENT '成功数量',
  `proc_warn` bigint DEFAULT 0 COMMENT '警告数量',
  `res_code` varchar(16) DEFAULT '00' COMMENT '执行结果代码',
  `res_message` text COMMENT '执行错误信息',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人ID',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改日期',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) NULL DEFAULT '0' COMMENT '记录删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务历史记录';

-- ------------------------------------
-- 表：待处理的问题数据记录
-- ------------------------------------
CREATE TABLE IF NOT EXISTS `map_tray_chip_warn` (
  `id` bigint(20) NOT NULL COMMENT '记录ID',
  `retry_times` int(11) DEFAULT 0 COMMENT '重试次数',
  `last_proc_time` timestamp NULL COMMENT '上次处理时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='待处理的问题数据';

-- ------------------------------------
-- 表：数据重刷表
-- ------------------------------------
CREATE TABLE IF NOT EXISTS `map_tray_chip_rebuilt` (
  `id` varchar(32) NOT NULL COMMENT '重刷ID',
  `type` tinyint(4) NOT NULL COMMENT '重刷类型，1：按批次重刷，2：按开始记录ID重刷',
  `lot_nos` text DEFAULT NULL COMMENT '重刷批次号，允许多个，用逗号分隔',
  `start_no` bigint(20) DEFAULT NULL COMMENT '重刷开始记录ID',
  `status` varchar(32) DEFAULT NULL COMMENT '执行结果',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人ID',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据重刷表';


-- ------------------------------------
-- 表map_tray_chip_move加索引
-- ------------------------------------
ALTER TABLE `map_tray_chip_move` ADD INDEX `idx_chip_id`(`chip_id`); 
ALTER TABLE `map_tray_chip_move` ADD INDEX `idx_start_time`(`start_time`);

-- ------------------------------------
-- 正常处理过程
-- ------------------------------------
DELIMITER $$
DROP PROCEDURE IF EXISTS `map_tray_chip_normal_process`$$
CREATE PROCEDURE `map_tray_chip_normal_process`(OUT PROCESS_SUCS BIGINT, OUT PROCESS_WARN BIGINT, OUT NORMAL_BEGIN BIGINT, OUT NORMAL_END BIGINT) 
BEGIN 
    -- 分片大小
    DECLARE PARTITION_VAL BIGINT;
    
    -- 开始处理的记录ID
    SET NORMAL_BEGIN = 0;
    SELECT CAST(val AS SIGNED) INTO NORMAL_BEGIN
    FROM map_variable 
    WHERE id = 'last_move_id';
    
    IF NORMAL_BEGIN IS NULL THEN
        SET NORMAL_BEGIN = 0;
    END IF;
    
    -- 结束记录ID
    SET NORMAL_END = 0;
    SELECT MAX(id) INTO NORMAL_END 
    FROM map_tray_chip_move;
    
    part_proc:BEGIN
        -- 无数据则退出
        IF NORMAL_END IS NULL OR NORMAL_END = 0 OR NORMAL_BEGIN >= NORMAL_END THEN
            LEAVE part_proc;
        END IF;
        
        -- 分片大小
        SELECT CAST(val AS SIGNED) INTO PARTITION_VAL
        FROM map_variable 
        WHERE id = 'partition_val';
        
        -- 最小5000
        IF PARTITION_VAL IS NULL OR PARTITION_VAL < 10000 THEN
            SET PARTITION_VAL = 10000;
        END IF;
        
        SET @_begin = NORMAL_BEGIN;
        SET @_end = NULL;
        
        REPEAT
            -- 根据分区大小计算本次的结束记录
            SET @_val = NULL;
            SELECT id INTO @_val 
            FROM map_tray_chip_move 
            WHERE id > @_begin 
            ORDER BY id ASC 
            LIMIT 1 OFFSET PARTITION_VAL;
            
            IF @_val IS NULL OR @_val > NORMAL_END THEN
                SET @_end = NORMAL_END;
            ELSE 
                SET @_end = @_val;
            END IF;
            
            SET @proc_suc = 0;
            SET @proc_warn = 0;
            CALL map_tray_chip_inner_process(@_begin, @_end, NULL, @proc_suc, @proc_warn);
            
            SET PROCESS_SUCS = IFNULL(PROCESS_SUCS,0) + @proc_suc;
            SET PROCESS_WARN = IFNULL(PROCESS_WARN,0) + @proc_warn;
            
            SET @_begin = @_end;
            -- 更新开始处理的记录ID
            UPDATE map_variable SET val = @_begin WHERE id = 'last_move_id';
        UNTIL @_end = NORMAL_END
        END REPEAT;
	END; 
END$$
DELIMITER ;


-- ------------------------------------
-- 分片内部处理过程
-- ------------------------------------
DELIMITER $$
DROP PROCEDURE IF EXISTS `map_tray_chip_inner_process`$$
CREATE PROCEDURE `map_tray_chip_inner_process`(IN BEGIN_ID BIGINT, IN END_ID BIGINT, IN SPEC_LOT_NO VARCHAR(32), OUT PROC_SUCS BIGINT, OUT PROC_WARN BIGINT) 
BEGIN 
    -- 创建临时表并插入定位数据
    CREATE TEMPORARY TABLE tmp_chiped_curt(id bigint NOT NULL);
    IF SPEC_LOT_NO IS NOT NULL THEN
        INSERT INTO tmp_chiped_curt 
        SELECT id 
            FROM map_tray_chip_move 
            WHERE lot_no = SPEC_LOT_NO
            AND eqp_id IN (SELECT eqp_id FROM map_equipment_config WHERE eqp_type = 8)
            AND chip_id IS NOT NULL;
    ELSE 
        INSERT INTO tmp_chiped_curt
        SELECT id 
            FROM map_tray_chip_move 
            WHERE id > BEGIN_ID
            AND id <= END_ID
            AND eqp_id IN (SELECT eqp_id FROM map_equipment_config WHERE eqp_type = 8)
            AND chip_id IS NOT NULL;
    END IF;
    
    procs_label: LOOP
    
        -- 查询需要递归追溯的记录行数
        SELECT COUNT(1) INTO @counts FROM tmp_chiped_curt;
        
        -- 无数据则退出
        IF @counts = 0 THEN
            LEAVE procs_label;
        END IF;
        
        -- 1. 查找上游需要处理的记录ID,插入到临时表tmp_chiped_next
        CREATE TEMPORARY TABLE tmp_chiped_next(
            id bigint,      -- 本次需要处理的记录ID
            down_id bigint  -- 下游已经处理的记录ID，由于是向前追溯，所以是下游数据
        ) SELECT id,down_id FROM (
            SELECT id,down_id, @num:=IF(@chip_eqp = CONCAT(chip_id,eqp_id), @num + 1, 1) AS num, @chip_eqp:=CONCAT(chip_id,eqp_id) AS chip_eqp
            FROM (
                SELECT a.chip_id, b.eqp_id, b.id, a.down_id, b.start_time
                FROM map_tray_chip_move b,
                (
                    SELECT cp.id AS down_id, cp.lot_no,cp.chip_id,cp.start_time,cnf.eqp_id,
                    -- 如果是报告类型的数据，没有from信息，取to信息
                    IF(cnf.eqp_type = 2, cp.to_tray_id, cp.from_tray_id) AS from_tray_id,
                    IF(cnf.eqp_type = 2, cp.to_x, cp.from_x) AS from_x,
                    IF(cnf.eqp_type = 2, cp.to_y, cp.from_y) AS from_y
                    FROM map_tray_chip_move cp, tmp_chiped_curt tmp, map_equipment_config cnf
                    WHERE cp.id = tmp.id
                    AND cp.eqp_id = cnf.eqp_id
                )a, map_equipment_config c
                WHERE a.lot_no = b.lot_no
                AND a.from_tray_id = b.to_tray_id 
                AND a.from_x = b.to_x 
                AND a.from_y = b.to_y 
                -- a表为下游
                AND a.eqp_id = c.down_eqp_id
                -- b表为上游
                AND b.eqp_id = c.eqp_id
                -- 下游时间晚于上游时间
                AND a.start_time > b.start_time
            )t,(SELECT @num := 0,@chip_eqp:='') tt
            ORDER BY chip_id, start_time DESC
        ) res 
        WHERE num = 1;
        
        -- 处理重复定位问题，这里直接删除，则下一步会被记录到待处理异常数据中
        DELETE tmp
        FROM tmp_chiped_next tmp, map_tray_chip_move a, map_tray_chip_move b
        WHERE tmp.id = a.id
        -- b表为下游数据
        AND tmp.down_id = b.id
        -- 之前已经被识别追溯
        AND a.chip_id IS NOT NULL
        -- 但追溯ID不一致
        AND a.chip_id != b.chip_id;
        
        -- 更新下一层数据的chip_id
        UPDATE map_tray_chip_move a, tmp_chiped_next tmp, map_tray_chip_move b
        SET a.chip_id = b.chip_id
        WHERE a.id = tmp.id
        AND tmp.down_id = b.id;
        
        -- 更新成功处理的记录行数
        SET PROC_SUCS = IFNULL(PROC_SUCS,0) + ROW_COUNT();
        
        -- 记录无上级流程的无头数据，视为异常数据
        INSERT IGNORE INTO map_tray_chip_warn(id)
        SELECT c.id 
        FROM tmp_chiped_curt c
        WHERE NOT EXISTS(
            SELECT 1 FROM tmp_chiped_next n WHERE c.id = n.down_id
        );
        
        -- 更新异常数据记录行数
        SET PROC_WARN = IFNULL(PROC_WARN,0) + ROW_COUNT();
        
        -- 删除next表中源头数据，不需要再往上查找
        DELETE tmp
        FROM tmp_chiped_next tmp, map_tray_chip_move a, map_equipment_config cnf
        WHERE tmp.id = a.id
        AND a.eqp_id = cnf.eqp_id
        AND cnf.eqp_type = 1;
        
        -- 交换临时表
        DROP TEMPORARY TABLE IF EXISTS tmp_chiped_curt;
        ALTER TABLE tmp_chiped_next DROP COLUMN down_id;
        ALTER TABLE tmp_chiped_next RENAME TO tmp_chiped_curt;      
        
    END LOOP;
    
    DROP TEMPORARY TABLE IF EXISTS tmp_chiped_curt;
        
END$$
DELIMITER ;

-- ------------------------------------
-- 数据重刷处理过程
-- ------------------------------------
DELIMITER $$
DROP PROCEDURE IF EXISTS `map_tray_chip_rebuilt_process`$$
CREATE PROCEDURE `map_tray_chip_rebuilt_process`(IN REBUILT_ID VARCHAR(32)) 
BEGIN
    -- 设置事件Id
	DECLARE CONN_ID BIGINT DEFAULT CONNECTION_ID();
    DECLARE LAST_CONN VARCHAR(32);
    -- 执行结果
    DECLARE REBUILT_STAT VARCHAR(32);
    
	-- 设置错误处理器
	DECLARE EXIT HANDLER FOR SQLEXCEPTION,NOT FOUND 
	BEGIN 
        -- 还原事务级别
		SET @@session.tx_isolation = @@tx_isolation;
        -- 重置CONN_ID
		UPDATE map_variable SET val = NULL WHERE id = 'last_conn_id' AND val = CAST(CONN_ID AS CHAR);
        -- 设置失败信息
        UPDATE map_tray_chip_rebuilt SET status = 'ERROR' WHERE id = REBUILT_ID;
	END; 
    
    proc_label:BEGIN
        -- 参数检查
        IF REBUILT_ID IS NULL THEN
            SET REBUILT_STAT = 'INVALID_ID';
            LEAVE proc_label;
        END IF;
        -- 获取rebuilt信息
        SELECT COUNT(1) INTO @_rows FROM map_tray_chip_rebuilt WHERE id = REBUILT_ID;
        IF @_rows = 0 THEN
            SET REBUILT_STAT = 'INVALID_ID';
            LEAVE proc_label;
        END IF;
        SELECT `type`, TRIM(`lot_nos`), `start_no` INTO @re_type, @re_lot_nos, @re_start_no
        FROM map_tray_chip_rebuilt
        WHERE id = REBUILT_ID;
        -- 校验
        IF @re_type IS NULL THEN
            SET REBUILT_STAT = 'INVALID_ID';
            LEAVE proc_label;
        END IF;
        IF @re_type NOT IN (1,2) THEN
            SET REBUILT_STAT = 'INVALID_TYPE';
            LEAVE proc_label;
        END IF;
        IF @re_type = 1 AND (@re_lot_nos IS NULL OR @re_lot_nos = '') THEN
            SET REBUILT_STAT = 'INVALID_LOT';
            LEAVE proc_label;
        END IF;
        IF @re_type = 2 AND @re_start_no IS NULL THEN
            SET REBUILT_STAT = 'INVALID_START';
            LEAVE proc_label;
        END IF;
        
        -- 检查正在执行的任务
        SELECT val into LAST_CONN FROM map_variable WHERE id = 'last_conn_id';
        IF LAST_CONN IS NOT NULL THEN
            SET REBUILT_STAT = 'OTHER_RUNNING';
            LEAVE proc_label;
        END IF;
        -- 设置CONN
        UPDATE map_variable SET val = CAST(CONN_ID AS CHAR) WHERE id = 'last_conn_id';
        -- 设置事务级别
        SET @tx := @@session.tx_isolation,@@session.tx_isolation = 'READ-UNCOMMITTED';
        
        -- ###### 开始处理重刷 ######
		
        IF @re_type = 1 THEN
            -- 按逗号分隔
            SET @delim = ',';
            SET @s_pos = 1;
            REPEAT
                SET @e_pos = LOCATE(@delim, @re_lot_nos, @s_pos);
                IF @e_pos = 0 THEN
                    SET @item =  TRIM(SUBSTR(@re_lot_nos, @s_pos));
                ELSE
                    SET @item =  TRIM(SUBSTR(@re_lot_nos, @s_pos, @e_pos - @s_pos));
                    SET @s_pos = @e_pos + 1;
                END IF;
                IF @item IS NOT NULL AND @item != '' THEN           
                    -- 删除该批次的chip_id
                    UPDATE map_tray_chip_move a, map_equipment_config cnf
                    SET a.chip_id = NULL
                    WHERE a.lot_no = @item
                    AND a.eqp_id = cnf.eqp_id
                    AND cnf.eqp_type != 8
                    AND a.chip_id IS NOT NULL;
                    
                    -- 删除待处理的异常数据
                    DELETE w
                    FROM map_tray_chip_warn w, map_tray_chip_move a
                    WHERE w.id = a.id
                    AND a.lot_no = @item;
                    
                    -- 调用内部处理存储过程
                    SET @out_suc = 0;
                    SET @out_warn = 0;
                    CALL map_tray_chip_inner_process(NULL, NULL, @item, @out_suc, @out_warn);
                END IF;                
            UNTIL @e_pos = 0
            END REPEAT;
        ELSE
            -- 删除ID>=X的记录行
            UPDATE map_tray_chip_move a, map_equipment_config cnf
            SET a.chip_id = NULL
            WHERE a.id >= @re_start_no
            AND a.eqp_id = cnf.eqp_id
            AND cnf.eqp_type != 8
            AND a.chip_id IS NOT NULL;
            
            -- 删除待处理异常数据中ID>=X的数据
            DELETE w
            FROM map_tray_chip_warn w
            WHERE w.id >= @re_start_no;
            
            -- 设置开始记录ID标志位
            UPDATE map_variable SET val = @re_start_no WHERE id = 'last_move_id';
            
            -- 调用正常处理存储过程
            SET @out_suc = 0;
            SET @out_warn = 0;
            SET @out_begin = 0;
            SET @out_end = 0;
            CALL map_tray_chip_normal_process(@out_suc, @out_warn, @out_begin, @out_end);
            
        END IF;
		-- ###### 结束处理重刷 ######
        SET REBUILT_STAT = 'SUCCESS';
        -- 还原事务级别
        SET @@session.tx_isolation = @tx;
        -- 重置CONN_ID
        UPDATE map_variable SET val = NULL WHERE id = 'last_conn_id' AND val = CAST(CONN_ID AS CHAR);
    END; 
    
    IF REBUILT_STAT != 'INVALID_ID' THEN
        UPDATE map_tray_chip_rebuilt SET status = REBUILT_STAT WHERE id = REBUILT_ID;
    END IF;
END$$
DELIMITER ;

-- ------------------------------------
-- 定时事件
-- ------------------------------------
DELIMITER $$
DROP EVENT IF EXISTS `map_tray_chip_job`$$
CREATE EVENT `map_tray_chip_job` ON SCHEDULE EVERY 10 MINUTE ON COMPLETION PRESERVE ENABLE DO 
BEGIN 
    -- 事件Id
	DECLARE VAR_JOB_ID VARCHAR(32) DEFAULT CAST(UUID_SHORT() AS CHAR);
    -- CONN_ID
	DECLARE CONN_ID BIGINT DEFAULT CONNECTION_ID();
    DECLARE TOTAL_SUC BIGINT DEFAULT 0;
    DECLARE TOTAL_WARN BIGINT DEFAULT 0;
    DECLARE BEGIN_ID BIGINT DEFAULT 0;
    DECLARE END_ID BIGINT DEFAULT 0;
	-- 设置错误处理器
	DECLARE EXIT HANDLER FOR SQLEXCEPTION,NOT FOUND 
	BEGIN 
		-- 获取错误信息
		GET DIAGNOSTICS CONDITION 1 @err_stat = RETURNED_SQLSTATE,@err_no = MYSQL_ERRNO,@err_msg = MESSAGE_TEXT;
        -- 还原事务级别
		SET @@session.tx_isolation = @@tx_isolation;
        -- 重置CONN_ID
        UPDATE map_variable SET val = NULL WHERE id = 'last_conn_id' AND val = CAST(CONN_ID AS CHAR);
		-- 记录错误信息
		UPDATE map_tray_chip_log 
		SET end_time = SYSDATE(), res_code = IF(@err_stat IS NULL,'00',@err_stat), res_message = CONCAT(@err_no,': ',@err_msg) 
		WHERE id = VAR_JOB_ID;
	END; 		
	
	main_proc:BEGIN
        -- 查询上次未完成的任务ID
        SET @last_conn := NULL;
        SELECT CAST(val AS UNSIGNED) INTO @last_conn 
        FROM map_variable WHERE id = 'last_conn_id';
        
        -- 上次任务未结束，本次跳过
        IF @last_conn IS NOT NULL THEN
            SELECT COUNT(1) INTO @last_conn 
            FROM information_schema.PROCESSLIST 
            WHERE ID = @last_conn;
            IF @last_conn > 0 THEN
                LEAVE main_proc;
            END IF;
        END IF;
        
        -- 记录本次执行的CONN_ID
        UPDATE map_variable SET val = CAST(CONN_ID AS CHAR) WHERE id = 'last_conn_id';
        -- 记录事件开始
        INSERT INTO map_tray_chip_log(id,begin_time) VALUES (VAR_JOB_ID,SYSDATE());
		-- 设置事务级别
		SET @tx := @@session.tx_isolation,@@session.tx_isolation = 'READ-UNCOMMITTED';
		
		-- ###### 业务逻辑开始 ######
		CALL map_tray_chip_normal_process(TOTAL_SUC, TOTAL_WARN, BEGIN_ID, END_ID);
		-- ###### 业务逻辑结束 ######
        
        -- 还原事务级别
		SET @@session.tx_isolation = @tx;
        -- 重置CONN_ID
        UPDATE map_variable SET val = NULL WHERE id = 'last_conn_id';
        -- 记录正常结束时间，成功数量，警告数量，总数
        UPDATE map_tray_chip_log 
        SET end_time = SYSDATE(), proc_suc = TOTAL_SUC, 
            proc_warn = TOTAL_WARN, proc_total = TOTAL_SUC + TOTAL_WARN,
            begin_no = BEGIN_ID, end_no = END_ID
        WHERE id = VAR_JOB_ID;	
	END; 	  
END$$
DELIMITER ;
