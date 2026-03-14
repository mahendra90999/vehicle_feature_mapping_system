package com.example.jpa.jpa1.Repositoroy;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.jpa.jpa1.Entity.ApplyMapping;
import com.example.jpa.jpa1.Entity.Country;
import com.example.jpa.jpa1.Entity.Feature;
import com.example.jpa.jpa1.Entity.Vehicle;

public interface MappingRepository extends JpaRepository<ApplyMapping, Integer>,JpaSpecificationExecutor<ApplyMapping> {

    boolean existsByFeatureAndVehicleAndCountry(
    		Feature feature,
            Vehicle vehicle,
            Country country
    );
    
    ApplyMapping findByFeatureAndVehicleAndCountry(
    		Feature feature,
            Vehicle vehicle,
            Country country
            );
    
    
    //to avoid n+1 problem
    @Override
    @EntityGraph(attributePaths = {"feature", "vehicle", "country"})
    Page<ApplyMapping> findAll(Specification<ApplyMapping> spec, Pageable pageable);

}