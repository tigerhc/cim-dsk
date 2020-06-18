package com.lmrj.cim.modules.sys.service.impl;

import com.lmrj.cim.config.autoconfigure.ShiroConfigProperties;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmrj.core.sys.entity.User;

@Service("passwordService")
public class PasswordService {
	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

	@Autowired
	private ShiroConfigProperties shiroConfigProperties;

	public void encryptPassword(User user) {

		user.setSalt(randomNumberGenerator.nextBytes().toHex());
		String newPassword = new SimpleHash(shiroConfigProperties.getCredentialsHashAlgorithmName(), user.getPassword(),
				ByteSource.Util.bytes(user.getCredentialsSalt()), shiroConfigProperties.getCredentialsHashIterations()).toHex();
		user.setPassword(newPassword);
	}

	public String getPassword(String password,String credentialsSalt) {
		String newPassword = new SimpleHash(shiroConfigProperties.getCredentialsHashAlgorithmName(), password,
				ByteSource.Util.bytes(credentialsSalt), shiroConfigProperties.getCredentialsHashIterations()).toHex();
		return newPassword;
	}
}
