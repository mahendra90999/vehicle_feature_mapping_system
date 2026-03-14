package com.example.jpa.jpa1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.jpa1.Dto.CountryDto;
import com.example.jpa.jpa1.Service.countryService;

@RestController
@RequestMapping("/countries")
public class CountryController {

	@Autowired
	countryService countryService;
	
	
	@GetMapping("/add")
	public CountryDto addData(@RequestBody CountryDto countryDto) {	
		return countryService.addData(countryDto);
	}
}
