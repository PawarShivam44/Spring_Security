package com.example.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.security.entity.UserInfo;
import com.example.security.repository.UserInfoRepo;
@Service
public class UserInfoService {

	@Autowired
	private UserInfoRepo repo;

	public String addUserDetails(UserInfo user) {

		repo.save(user);

		return "User Added Sucessfully....";
	}

}
