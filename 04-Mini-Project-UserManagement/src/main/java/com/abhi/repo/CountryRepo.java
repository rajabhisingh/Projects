package com.abhi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abhi.entity.CountryEntity;

@Repository
public interface CountryRepo extends JpaRepository<CountryEntity, Integer>{

}
