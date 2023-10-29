package com.jsp.et.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsp.et.dto.ImageDTO;
import com.jsp.et.dto.UserDTO;
import com.jsp.et.entity.Image;
import com.jsp.et.entity.User;
import com.jsp.et.repository.UserRepository;

//business logic
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public int registration(UserDTO dto) {
		// create object of an entity class
		User userdb = new User();
		// transfer data from dto to entity object
		/*
		 * Basic way userdb.setUserName(dto.getUserName);
		 * userdb.setFullName(dto.getFullName);
		 */
		if (dto.getPassword().equals(dto.getRepassword())) {
			BeanUtils.copyProperties(dto, userdb);
			return userRepository.save(userdb).getUserId();
		}
		// userRepository.save(userdb);
		return 0;
	}

	@Override
	public UserDTO login(UserDTO userdto) {
		Optional<User> findByUserNameAndPassword = userRepository.findByUserNameAndPassword(userdto.getUserName(),
				userdto.getPassword());
		UserDTO verifiedUser = new UserDTO();
		
		if (findByUserNameAndPassword.isPresent()) {
			
			User userdb = findByUserNameAndPassword.get();
			BeanUtils.copyProperties(userdb, verifiedUser);
			
			//to store imagedto object into userdto
			if(userdb.getImage() != null) {
				ImageDTO imageDTO = new ImageDTO();
				BeanUtils.copyProperties(userdb.getImage(), imageDTO);
				verifiedUser.setImage(imageDTO);
			}
			return verifiedUser;
		}
		return null;
	}

	@Override
	public UserDTO findByUserId(int userId) {
		UserDTO dto = new UserDTO();
		BeanUtils.copyProperties(userRepository.findById(userId).get(), dto);
		return dto;
	}

	@Override
	public UserDTO updateUserProfile(int userId, UserDTO userDTO) {
		User user = userRepository.findById(userId).get();
		user.setEmail(userDTO.getEmail());
		user.setFullName(userDTO.getFullName());
		user.setMobile(userDTO.getMobile());
		user.setPassword(userDTO.getPassword());
		user.setUserName(userDTO.getUserName());

		//to add image object into user object
		if (userDTO.getImage().getData().length != 0) {
			Image image = new Image();
			BeanUtils.copyProperties(userDTO.getImage(), image);
			user.setImage(image);
		}

		UserDTO updated = new UserDTO();
		BeanUtils.copyProperties(userRepository.save(user), updated);
		return updated;
	}

	@Override
	public int deleteUserProfile(int userId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			userRepository.delete(user.get());
			return 1;
		}
		return 0;
	}

}
