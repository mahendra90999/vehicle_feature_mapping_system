package com.example.jpa.jpa1.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.jpa1.Entity.Feature;

public interface FeatureDataRepository extends JpaRepository<Feature, Integer>{
	
	Optional<Feature> findByFeatureName(String FeatureName);
	
	Page<Feature> findByFeatureNameContainingIgnoreCase(String name,Pageable pageable);

	
}
