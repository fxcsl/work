package com.sinovatio.owls.business.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinovatio.owls.base.BaseService;
import com.sinovatio.owls.business.demo.dao.UserDao;
import com.sinovatio.owls.business.demo.entity.User;
import com.sinovatio.owls.util.RedisUtil;

@Service
public class UserService extends BaseService {
	@Autowired
	public UserDao userDao;

	@Autowired
	private RedisUtil redisUtil;

	public User findUserById(Long id) {
		String key = "user_" + id;
		boolean hasKey = redisUtil.exists(key);
		if (hasKey) {
			User user = (User)redisUtil.get(key);
			System.out.println("UserService.findUserById() : 从缓存中获取了用戶 >> " + user.toString());
			return user;
		}

		User user = userDao.findById(id);
		redisUtil.set(key, user);
		System.out.println("UserService.findUserById() : 将用戶插入缓存 >> " + user.toString());

		return user;
	}
	
	public List<User> findAllUser(){
        return userDao.findAllUser();
    }
	
	public void procedure(Map<String, String> map) {
		userDao.procedure(map);
	}

	public void procedure1(Map<String, String> map) {
		userDao.procedure1(map);
	}
}
