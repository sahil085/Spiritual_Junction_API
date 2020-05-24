package com.spiritual.junction.iyfAPI.securityservices;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiritual.junction.iyfAPI.domain.User;
import com.spiritual.junction.iyfAPI.repository.UserRepository;

/**
 * @author kamal berriga
 *
 */
@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public User save(User user) {
		return userRepository.saveAndFlush(user);
	}

	public User update(User user) {
		return userRepository.save(user);
	}

	public User find(String userName) {
		return userRepository.findByEmail(userName);
	}

	public User find(Long id) {
		return userRepository.getOne(id);
	}

	public List<String> findAllEmails() {
		return userRepository.findAllEmail();
	}
}
