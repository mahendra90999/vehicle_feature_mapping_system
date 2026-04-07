package com.example.jpa.jpa1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.jpa1.Dto.VehicleDto;
import com.example.jpa.jpa1.Service.VehicleService;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
	
	
	@Autowired
	VehicleService vehicleService;
	
	@PostMapping("/add")
	public VehicleDto addData(@RequestBody VehicleDto vehicleDto) {
		System.out.println(vehicleDto.getVariant());  
		return vehicleService.addData(vehicleDto);
	}
}
