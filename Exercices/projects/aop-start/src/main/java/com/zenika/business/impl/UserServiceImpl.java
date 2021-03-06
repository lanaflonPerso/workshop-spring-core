/**
 * 
 */
package com.zenika.business.impl;

import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zenika.business.UserService;
import com.zenika.domain.User;
import com.zenika.repository.UserRepository;

/**
 * @author acogoluegnes
 *
 */
public class UserServiceImpl implements UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	private UserRepository userRepository;
	
	private String digest;
	
	private Encoder encoder;
	
	/* (non-Javadoc)
	 * @see com.zenika.business.UserService#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public User authenticate(String login, String password) {
		User user = userRepository.getByLogin(login);
		if(user == null) {
			return null;
		} else {
			if(password == null) {
				throw new IllegalArgumentException("Password cannot be empty!");
			}
			password = encoder.encode(password);
			if(password.equals(user.getPassword())) {
				return user;
			} else {
				return null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.zenika.business.UserService#create(java.lang.String, java.lang.String)
	 */
	@Override
	public User create(String login, String password) {
		User user = userRepository.getByLogin(login);
		if(user == null) {
			user = userRepository.create(login, encoder.encode(password));
		} else {
			throw new IllegalArgumentException("A user with the same login already exists");
		}
		return user;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.zenika.business.UserService#list()
	 */
	@Override
	public List<User> list() {
		return userRepository.list();
	}
	
	public void init() {
		if(digest == null || digest.trim().length() == 0) {
			LOGGER.info("No password hashing");
			encoder = new Encoder() {
				@Override
				public String encode(String input) {
					return input;
				}
			};
		} else {
			LOGGER.info("Using {} algorithm for password hashing",digest);
			encoder = new MessageDigestEncoder(digest);
		}
	}
	
	private interface Encoder {
		
		String encode(String input);
		
	}
	
	private class MessageDigestEncoder implements Encoder {
		
		private final String algorithm;
		
		public MessageDigestEncoder(String algorithm) {
			this.algorithm = algorithm;
		}
		
		@Override
		public String encode(String input) {
			byte[] bytes = StringUtils.getBytesUtf8(input);
			return Hex.encodeHexString(DigestUtils.getDigest(algorithm).digest(bytes));
		}
	}
	
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void setDigest(String digest) {
		this.digest = digest;
	}
	
}
