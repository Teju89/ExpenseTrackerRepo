package com.jsp.et.service;

import com.jsp.et.dto.UserDTO;

public interface UserService {
	int registration(UserDTO dto);
	
	//UserDTO login(String username, String password);
	UserDTO login(UserDTO userdto);
	
	UserDTO findByUserId(int userId);
	
	UserDTO updateUserProfile(int userId, UserDTO userDTO);
	
	int deleteUserProfile(int userId);
	
}





