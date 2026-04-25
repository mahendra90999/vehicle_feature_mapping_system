package com.example.jpa.jpa1.Repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.jpa1.Entity.Vehicle;

public interface VehicleDataRepository extends JpaRepository<Vehicle, Integer>{
	
	Optional<Vehicle> findByVehicleName(String vehicleName);

	Page<Vehicle> findByVehicleNameContainingIgnoreCase(String name,Pageable pageable);

	
}
