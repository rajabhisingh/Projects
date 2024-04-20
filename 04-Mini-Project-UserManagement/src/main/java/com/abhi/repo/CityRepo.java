package com.abhi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abhi.entity.CityEntity;

@Repository
public interface CityRepo extends JpaRepository<CityEntity, Integer> {

	@Query(value = "select *from CITY_MASTER where state_id=:stateId", nativeQuery = true)
	public List<CityEntity> getCities(Integer stateId);
}
