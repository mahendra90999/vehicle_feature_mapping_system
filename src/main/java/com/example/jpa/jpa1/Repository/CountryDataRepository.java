package com.example.jpa.jpa1.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.jpa1.Entity.Country;


public interface CountryDataRepository extends JpaRepository<Country, Integer>{
	
	Optional<Country> findByCountryName(String CountryName);

	Page<Country> findByCountryNameContainingIgnoreCase(String name, Pageable pageable);

}
