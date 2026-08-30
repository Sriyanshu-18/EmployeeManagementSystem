package com.ems.service;

import com.ems.dto.UserDTO;
import com.ems.entity.User;

public interface UserService {

    User registerUser(UserDTO userDTO);
}