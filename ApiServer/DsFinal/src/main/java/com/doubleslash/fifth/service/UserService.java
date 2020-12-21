package com.doubleslash.fifth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doubleslash.fifth.repository.UserRepository;
import com.doubleslash.fifth.vo.UserVO;
import com.google.common.base.Optional;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	//User 중복 체크 & User 추가
	public boolean insertUser(String uid) {
		Optional<UserVO> users = userRepository.findByUid(uid);
		if(users.isPresent()) {
			return false;
		}else {
			UserVO user = new UserVO();
			user.setUid(uid);
			userRepository.save(user);
			return true;
		}
	}
	
}
