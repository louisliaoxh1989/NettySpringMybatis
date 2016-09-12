package com.ycg.ycexpress.server.impl;

import org.springframework.stereotype.Service;

import com.ycg.ycexpress.beans.User;
import com.ycg.ycexpress.server.UserService;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User, Integer> 
	implements UserService {
	
	
}
