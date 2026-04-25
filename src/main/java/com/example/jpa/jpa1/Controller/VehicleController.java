package com.example.jpa.jpa1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.jpa1.Dto.ApiResponseDto;
import com.example.jpa.jpa1.Dto.VehicleDto;
import com.example.jpa.jpa1.Service.VehicleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
	
	
	@Autowired
	VehicleService vehicleService;
	
	@GetMapping("/{name}")
	public ApiResponseDto<?> showAllData(@PathVariable String name,@RequestParam(defaultValue = "0") int page){
		return vehicleService.showData(name,page);
	}
	
	@PostMapping("/add")
	public VehicleDto addData(@RequestBody VehicleDto vehicleDto) {
		return vehicleService.addData(vehicleDto);
	}
	
	@PutMapping("/update/{id}")
		public ApiResponseDto<VehicleDto> updateData(@PathVariable int id,@Valid @RequestBody VehicleDto dto){
			return vehicleService.updateData(id,dto);
		}
	
	@DeleteMapping("/delete/{id}")
	public ApiResponseDto<String> deleteData(@PathVariable int id){
		return vehicleService.deleteData(id);
	} 
		
	
}
