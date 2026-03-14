package com.example.jpa.jpa1.Service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Dto.VehicleDto;
import com.example.jpa.jpa1.Entity.Vehicle;
import com.example.jpa.jpa1.Repositoroy.VehicleDataRepository;

@Service
public class VehicleService {
	
	@Autowired
	VehicleDataRepository vehicleDataRepository;
	
	@Autowired
	ModelMapper modelMapper;

	public VehicleDto addData(VehicleDto vehicleDto) {
		
		Vehicle vehicle = Vehicle.builder()
							.companyName(vehicleDto.getCompany_name())
							.vehicleName(vehicleDto.getVehicle_name())
							.variant(vehicleDto.getVariant())
							.build();
		
		Vehicle saveVehicle = vehicleDataRepository.save(vehicle);
		
		VehicleDto vehicleobj = modelMapper.map(saveVehicle, VehicleDto.class);
		
		return vehicleobj;
	}
	
	
}
