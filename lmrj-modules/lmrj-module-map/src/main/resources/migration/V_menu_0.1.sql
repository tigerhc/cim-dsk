INSERT IGNORE INTO `sys_menu`(`id`, `name`, `type`, `path`, `component`, `parent_id`, `parent_ids`, `permission`, `project_id`, `enabled`, `url`, `sort`, `icon`, `redirect`, `cacheable`, `require_auth`, `remarks`, `create_by`, `create_date`, `del_flag`) 
VALUES 
('0c2168222cd050684632b4606e5f2932', '追溯管理', '1', 'map', 'Layout', '8DB8D05ADADF6BE3E0530300A8C02C36', '8DB8D05ADADF6BE3E0530300A8C02C36', NULL, '2', 1, NULL, 2, 'meun', NULL, 1, 1, '1', '2', CURRENT_TIMESTAMP(), '0'),
('de4b14e4c4d6170b69ac58f9e8ca4816', 'Tray盘设置', '2', 'trayconfig', 'views/map/tray/trayconfigList', '0c2168222cd050684632b4606e5f2932', '8DB8D05ADADF6BE3E0530300A8C02C36/0c2168222cd050684632b4606e5f2932/', NULL, '2', 1, NULL, 31, 'fa-sun-o', NULL, 1, 1, NULL, '2', CURRENT_TIMESTAMP(), '0'),
('6463e35462ea46148173b96b2d32eee8', 'Tray盘设置-修改', '4', 'trayconfigEdit', 'views/map/tray/trayconfigEdit', 'de4b14e4c4d6170b69ac58f9e8ca4816', '8DB8D05ADADF6BE3E0530300A8C02C36/0c2168222cd050684632b4606e5f2932/de4b14e4c4d6170b69ac58f9e8ca4816/', NULL, '2', 1, '', 0, '', NULL, 1, 1, NULL, '2', CURRENT_TIMESTAMP(), '0'),
('fed15baa979a46c1a39c66aef6f9bc6b', 'Eqp关系设置', '4', 'eqpconfig', 'views/map/tray/eqpconfigList', 'de4b14e4c4d6170b69ac58f9e8ca4816', '8DB8D05ADADF6BE3E0530300A8C02C36/0c2168222cd050684632b4606e5f2932/de4b14e4c4d6170b69ac58f9e8ca4816/', NULL, '2', 1, '', 0, '', NULL, 1, 1, NULL, '2', CURRENT_TIMESTAMP(), '0'),
('d92643809c0a41d5903d31e311895897', '追溯查询', '2', 'traymovequery', 'views/map/tray/traychipmoveList', '0c2168222cd050684632b4606e5f2932', '8DB8D05ADADF6BE3E0530300A8C02C36/0c2168222cd050684632b4606e5f2932/', NULL, '2', 1, NULL, 31, 'fa-sun-o', NULL, 1, 1, NULL, '2', CURRENT_TIMESTAMP(), '0'),
('b9148dc5c9b84140888400d1cebebcf9', 'Chip追溯信息', '4', 'traymoveDetail', 'views/map/tray/traychipmoveDetail', 'd92643809c0a41d5903d31e311895897', '8DB8D05ADADF6BE3E0530300A8C02C36/0c2168222cd050684632b4606e5f2932/d92643809c0a41d5903d31e311895897/', NULL, '2', 1, '', 0, '', NULL, 1, 1, NULL, '2', CURRENT_TIMESTAMP(), '0'),
('eb228eb32c8845e588b8f302bdbc13f6', 'Chip任务历史', '4', 'trayjobhistory', 'views/map/tray/trayjobhistoryList', 'd92643809c0a41d5903d31e311895897', '8DB8D05ADADF6BE3E0530300A8C02C36/0c2168222cd050684632b4606e5f2932/d92643809c0a41d5903d31e311895897/', NULL, '2', 1, '', 0, '', NULL, 1, 1, NULL, '2', CURRENT_TIMESTAMP(), '0');

INSERT IGNORE INTO `sys_role_menu`(`id`, `menu_id`, `role_id`) 
VALUES 
('52df88ab2a6836d62b287d71e5e5a5d4', '0c2168222cd050684632b4606e5f2932', 'dc2c3456198b4c309b0bb3cac0660cc2'),
('274b8c57c10b80dfb3645d2c7a615e45', 'de4b14e4c4d6170b69ac58f9e8ca4816', 'dc2c3456198b4c309b0bb3cac0660cc2'),
('93408120a94c482cb80d48b53c4f158b', '6463e35462ea46148173b96b2d32eee8', 'dc2c3456198b4c309b0bb3cac0660cc2'),
('ed849e06454b4082a17aa3a1875a769c', 'fed15baa979a46c1a39c66aef6f9bc6b', 'dc2c3456198b4c309b0bb3cac0660cc2'),
('fed95a2a0f684679b3f9bf70e735493a', 'd92643809c0a41d5903d31e311895897', 'dc2c3456198b4c309b0bb3cac0660cc2'),
('42cc076ddab646ec88fc455e2b2099a3', 'b9148dc5c9b84140888400d1cebebcf9', 'dc2c3456198b4c309b0bb3cac0660cc2'),
('93728774a78945829eb3f957fe6f6a10', 'eb228eb32c8845e588b8f302bdbc13f6', 'dc2c3456198b4c309b0bb3cac0660cc2');