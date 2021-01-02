package com.doubleslash.fifth.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doubleslash.fifth.repository.UserRepository;
import com.doubleslash.fifth.vo.UserVO;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	// User 중복 체크 & User 추가
	public void insertUser(String uid){
		try {
			UserVO user = new UserVO(uid);
			userRepository.save(user);
		}catch(Exception e) {
			return;
		}
	}
	
	// 닉네임 중복 확인
	public UserVO nicknameCheck(String nickname) throws IOException {
		UserVO user = userRepository.findByNickname(nickname);
		return user;
	}
	
	// user 등록 
	public void registerUser(String uid, String nickname, Float drink, int hangover) {
		try {
			UserVO user = userRepository.findByUid(uid);
			user.setNickname(nickname);
			user.setDrink(drink);
			user.setHangover(hangover);
			userRepository.save(user);
		}catch(Exception e) {
			System.out.println("user 등록 에러 : " + e);
		}
	}

}
