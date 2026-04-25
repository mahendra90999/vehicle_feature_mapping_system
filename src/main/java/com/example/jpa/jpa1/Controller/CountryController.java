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
import com.example.jpa.jpa1.Dto.CountryDto;
import com.example.jpa.jpa1.Dto.VehicleDto;
import com.example.jpa.jpa1.Service.countryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/countries")
public class CountryController {

	@Autowired
	countryService countryService;
	
	
	@PostMapping("/add")
	public CountryDto addData(@RequestBody CountryDto countryDto) {	
		return countryService.addData(countryDto);
	}
	
	@GetMapping("/{name}")
	public ApiResponseDto<?> showData(@PathVariable String name,@RequestParam(defaultValue = "0") int page){
		return countryService.showData(name,page);
	}
	
	@PutMapping("/update/{id}")
	public ApiResponseDto<CountryDto> updateData(@PathVariable int id,@Valid @RequestBody CountryDto dto){
		return countryService.updateData(id,dto);
	}
	
	@DeleteMapping("/delete/{id}")
	public ApiResponseDto<String> deleteData(@PathVariable int id){
		return countryService.deleteData(id);
	}
	
}
