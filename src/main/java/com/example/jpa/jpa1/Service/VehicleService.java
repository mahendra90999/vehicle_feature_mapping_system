package com.example.jpa.jpa1.Service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Dto.VehicleDto;
import com.example.jpa.jpa1.Entity.Vehicle;
import com.example.jpa.jpa1.Repository.VehicleDataRepository;

@Service
public class VehicleService {
	
	@Autowired
	VehicleDataRepository vehicleDataRepository;
	
	@Autowired
	ModelMapper modelMapper;

	public VehicleDto addData(VehicleDto vehicleDto) {
		
		Vehicle vehicle = Vehicle.builder()
							.companyName(vehicleDto.getCompanyName())
							.vehicleName(vehicleDto.getVehicleName())
							.variant(vehicleDto.getVariant())
							.build();
		
		Vehicle saveVehicle = vehicleDataRepository.save(vehicle);
		
		VehicleDto vehicleobj = modelMapper.map(saveVehicle, VehicleDto.class);
		
		return vehicleobj;
	}
	
	
}
