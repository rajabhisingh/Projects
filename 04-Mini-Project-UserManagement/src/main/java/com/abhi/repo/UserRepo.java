 package com.abhi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abhi.entity.UserEntity;


@Repository
public interface  UserRepo extends JpaRepository<UserEntity, Integer> {
 
	public UserEntity  findByEmail(String email);
	
	public UserEntity findByEmailAndPwd(String email,String pwd);
}
