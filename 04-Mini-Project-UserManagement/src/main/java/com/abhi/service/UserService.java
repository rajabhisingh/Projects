package com.abhi.service;

import java.util.Map;

import com.abhi.dto.LoginDto;
import com.abhi.dto.RegisterDto;
import com.abhi.dto.ResetPwdDto;
import com.abhi.dto.UserDto;

public interface UserService {
	public Map<Integer,String> getCountries();

	public Map<Integer,String> getStates(Integer cid);

	public Map<Integer,String> getCities(Integer sid);

	public UserDto getUser(String email);

	public boolean registerUser(RegisterDto regDto);

	public UserDto getUser(LoginDto loginDto);

	public boolean resetPwd(ResetPwdDto pwdDto);

	public String getQuote(); // api-call

}
