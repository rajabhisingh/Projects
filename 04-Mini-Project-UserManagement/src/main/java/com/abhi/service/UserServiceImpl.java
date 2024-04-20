package com.abhi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.abhi.dto.LoginDto;
import com.abhi.dto.QuoteDto;
import com.abhi.dto.RegisterDto;
import com.abhi.dto.ResetPwdDto;
import com.abhi.dto.UserDto;
import com.abhi.entity.CityEntity;
import com.abhi.entity.CountryEntity;
import com.abhi.entity.StateEntity;
import com.abhi.entity.UserEntity;
import com.abhi.repo.CityRepo;
import com.abhi.repo.CountryRepo;
import com.abhi.repo.StateRepo;
import com.abhi.repo.UserRepo;
import com.abhi.util.EmailUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CountryRepo countryRepo;

	@Autowired
	private StateRepo stateRepo;

	@Autowired
	private CityRepo cityRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public Map<Integer, String> getCountries() {
		Map<Integer, String> countryMap = new HashMap<>();
		List<CountryEntity> countriesList = countryRepo.findAll();

		countriesList.forEach(c -> {
			countryMap.put(c.getCountryId(), c.getCountryName());
		});
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer cid) {

		Map<Integer, String> stateMap = new HashMap<>();

		/*
		 * CountryEntity country = new CountryEntity(); country.setCountryId(cid);
		 * 
		 * StateEntity entity = new StateEntity(); entity.setCountry(country);
		 * 
		 * Example<StateEntity> of = Example.of(entity);
		 * 
		 * List<StateEntity> statesList = stateRepo.findAll(of);
		 */
		List<StateEntity> statesList = stateRepo.getStates(cid);
		statesList.forEach(s -> {
			stateMap.put(s.getStateId(), s.getStateName());
		});

		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer sid) {

		Map<Integer, String> citiesMap = new HashMap<>();

		List<CityEntity> citiesList = cityRepo.getCities(sid);

		citiesList.forEach(c -> {
			citiesMap.put(c.getCityId(), c.getCityName());
		});
		return citiesMap;
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepo.findByEmail(email);

		if (userEntity == null) {

			return null;
		}

		/*
		 * UserDto dto = new UserDto(); BeanUtils.copyProperties(userEntity, dto);
		 */

		ModelMapper mapper = new ModelMapper();
		UserDto userDto = mapper.map(userEntity, UserDto.class);

		return userDto;
	}

	@Override
	public boolean registerUser(RegisterDto regDto) {

		ModelMapper mapper = new ModelMapper();

		UserEntity entity = mapper.map(regDto, UserEntity.class);

		CountryEntity country = countryRepo.findById(regDto.getCountryId()).orElseThrow();

		StateEntity state = stateRepo.findById(regDto.getStateId()).orElseThrow();

		CityEntity city = cityRepo.findById(regDto.getCityId()).orElseThrow();

		entity.setCountry(country);
		entity.setState(state);
		entity.setCity(city);

		entity.setPwd(generateRandom());
		entity.setPwdUpdate("NO");

		UserEntity saveEntity = userRepo.save(entity);

		String subject = "User Registration";

		String body = "Your temporary Pwd is:" + entity.getPwd();

		emailUtils.sendEmail(regDto.getEmail(), subject, body);
		return saveEntity.getUserId() != null;
	}

	@Override
	public UserDto getUser(LoginDto loginDto) {
		UserEntity userEntity = userRepo.findByEmailAndPwd(loginDto.getEmail(), loginDto.getPwd());
		if (userEntity == null) {
			return null;
		}
		ModelMapper mapper = new ModelMapper();
		return mapper.map(userEntity, UserDto.class);

	}

	@Override
	public boolean resetPwd(ResetPwdDto pwdDto) {
		UserEntity userEntity = userRepo.findByEmailAndPwd(pwdDto.getEmail(), pwdDto.getOldPwd());

		if (userEntity != null) {
			userEntity.setPwd(pwdDto.getNewPwd());

			userEntity.setPwdUpdate("YES");

			userRepo.save(userEntity);
			return true;
		}
		return false;
	}

	@Override
	public String getQuote() {
		QuoteDto[] quotations = null;

		String url = "https://type.fit/api/quotes";

		// web service call
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> forEntity = rt.getForEntity(url, String.class);
		String responseBody = forEntity.getBody();
		ObjectMapper mapper = new ObjectMapper();
		try {
			quotations = mapper.readValue(responseBody, QuoteDto[].class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Random r = new Random();
		int index = r.nextInt(quotations.length - 1);
		return quotations[index].getText();
	}

	private static String generateRandom() {
		String aToZ = "ABCDEFGHIJKLMNOPQSTUVWXYZ123456789";
		Random rand = new Random();
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			int randIndex = rand.nextInt(aToZ.length());
			res.append(aToZ.charAt(randIndex));
		}
		return res.toString();

	}

}
