package com.example.jpa.jpa1.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.jpa1.Entity.Country;
import com.example.jpa.jpa1.Entity.Feature;

public interface CountryDataRepository extends JpaRepository<Country, Integer>{
	
	Optional<Country> findByCountryName(String CountryName);

}
