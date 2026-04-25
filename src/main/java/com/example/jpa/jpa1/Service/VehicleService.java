package com.example.jpa.jpa1.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Dto.ApiResponseDto;
import com.example.jpa.jpa1.Dto.VehicleDto;
import com.example.jpa.jpa1.Dto.VehicleIdDto;
import com.example.jpa.jpa1.Entity.Vehicle;
import com.example.jpa.jpa1.Exception.ForeignkeyException.ForeignkeyException;
import com.example.jpa.jpa1.Repository.VehicleDataRepository;
import com.example.jpa.jpa1.ServiceInterface.VehicleServiceInterface;


@Service
public class VehicleService implements VehicleServiceInterface {
	
	@Autowired
	VehicleDataRepository vehicleDataRepository;

	private static final Logger log = LoggerFactory.getLogger(VehicleService.class);
	
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

	public ApiResponseDto<VehicleDto> updateData(int id, VehicleDto dto) {
		log.info("Updating vehicle with id: {}", id);
		
		Vehicle vehicle = vehicleDataRepository.findById(id).orElseThrow(() -> {
							log.error("vehicle not found");
							return new RuntimeException("Vehicle not found");
						});
		
		log.debug("Old Vehicle Data: {}", vehicle);
		
		vehicle.setVehicleName(dto.getVehicleName());
		vehicle.setVariant(dto.getVariant());
		vehicle.setCompanyName(dto.getCompanyName());
		vehicleDataRepository.save(vehicle);
		
		log.info("Vehicle updated successfully with id: {}", id);

		VehicleDto vehicleobj = modelMapper.map(vehicle, VehicleDto.class);
		
		log.debug("Updated Vehicle DTO: {}", vehicleobj);
		
		return new ApiResponseDto<VehicleDto>(true,"data has been updated",vehicleobj);
	}

	
	
	public ApiResponseDto<?> showData(String name,int page) {
		log.info("showing all data");
			
			Pageable pageable = PageRequest.of(page, 10);
			
			Page<Vehicle> vehiclePage =
		            vehicleDataRepository.findByVehicleNameContainingIgnoreCase(name, pageable);
			
			log.info("Total records found: {}", vehiclePage.getTotalElements());
			
			List<VehicleIdDto> listDto = vehiclePage.getContent().stream()
											.map(d -> modelMapper.map(d,VehicleIdDto.class))
											.toList();
		
		return new ApiResponseDto<>(true,"all vehicle data",listDto);
	}

	
	
	
	
	public ApiResponseDto<String> deleteData(int id) {
		
		Vehicle vehicle = vehicleDataRepository.findById(id).orElseThrow(() -> {
			log.error("vehicle not found");
			return new RuntimeException("vehicle not found");
		});
		
			try {
			vehicleDataRepository.delete(vehicle);
			}catch (Exception e) {
				log.error("Cannot delete or update a parent row: a foreign key constraint exception");
				throw new ForeignkeyException("Cannot delete or update "+vehicle.getVehicleName()+": a foreign key constraint exception kindly remove mapping first.");
			}
			
			log.info("Vehicle deleted successfully with id: {}", id);
		
		return new ApiResponseDto<String>(true,"Vehicle deleted successfully",null);
	}
	
	
	
}
