package com.abhi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abhi.entity.StateEntity;

@Repository
public interface StateRepo extends JpaRepository<StateEntity, Integer>{

	@Query(value = "select *from STATE_MASTER where country_id=:cid", nativeQuery = true)
	public List<StateEntity> getStates(Integer cid);
}
