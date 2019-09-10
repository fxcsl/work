package com.sinovatio.owls.business.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sinovatio.owls.business.demo.entity.User;

@Mapper
public interface UserDao{
	
	User findById(@Param("id") Long id);
	
	List<User> findAllUser();
	
	void procedure(Map<String, String> map);
	
	void procedure1(Map<String, String> map);
	
}
