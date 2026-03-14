package com.example.jpa.jpa1.Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Dto.CountryDto;
import com.example.jpa.jpa1.Entity.Country;
import com.example.jpa.jpa1.Repositoroy.CountryDataRepository;

@Service
public class countryService {
	
	@Autowired
	CountryDataRepository countryDataRepository;
	
	@Autowired
	ModelMapper modelMapper;

	public CountryDto addData(CountryDto countryDto) {
		
		Country country = modelMapper.map(countryDto, Country.class);
		country.setCountryName(countryDto.getCountry_name());
		
		Country countryobj = countryDataRepository.save(country);
		return modelMapper.map(countryobj, CountryDto.class);
	};
	
}