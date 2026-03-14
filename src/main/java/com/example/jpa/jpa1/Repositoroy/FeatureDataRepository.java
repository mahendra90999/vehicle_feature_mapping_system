package com.example.jpa.jpa1.Repositoroy;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.jpa1.Entity.Feature;

public interface FeatureDataRepository extends JpaRepository<Feature, Integer>{
	
	Optional<Feature> findByFeatureName(String FeatureName);

	
}
