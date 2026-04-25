package com.example.jpa.jpa1.Service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Dto.ApiResponseDto;
import com.example.jpa.jpa1.Dto.CountryDto;
import com.example.jpa.jpa1.Dto.CountryIdDto;
import com.example.jpa.jpa1.Entity.Country;
import com.example.jpa.jpa1.Exception.ResourceNotFoundException;
import com.example.jpa.jpa1.Exception.ForeignkeyException.ForeignkeyException;
import com.example.jpa.jpa1.Repository.CountryDataRepository;


@Service
public class countryService {
	
	@Autowired
	CountryDataRepository countryDataRepository;
	
	private final Logger log = LoggerFactory.getLogger(countryService.class);
	
	@Autowired
	ModelMapper modelMapper;

	public CountryDto addData(CountryDto countryDto) {
		
		Country country = modelMapper.map(countryDto, Country.class);
		country.setCountryName(countryDto.getCountryName());
		
		Country countryobj = countryDataRepository.save(country);
		return modelMapper.map(countryobj, CountryDto.class);
	};
	
	
//	searching country

	public ApiResponseDto<?> showData(String name,int page) {
		log.info("showing Country ");
			
			Pageable pageable = PageRequest.of(page, 10);
			
			Page<Country> countryPage =
		            countryDataRepository.findByCountryNameContainingIgnoreCase(name, pageable);
			
			log.info("Total records found: {}", countryPage.getTotalElements());
			
			List<CountryIdDto> listDto = countryPage.getContent().stream()
											.map(d -> modelMapper.map(d,CountryIdDto.class))
											.toList();
		
		return new ApiResponseDto<>(true,"all Country data",listDto);
	}

	
	
	
	
//	update country
	public ApiResponseDto<CountryDto> updateData(int id, CountryDto dto) {
		log.info("Updating Country with id: {}", id);
		
		Country country = countryDataRepository.findById(id).orElseThrow(() -> {
							log.error("country not found");
							return new RuntimeException("country not found");
						});
		
		log.debug("Old Vehicle Data: {}", country);
		
		country.setCountryName(dto.getCountryName());
		countryDataRepository.save(country);
		
		log.info("Country updated successfully with id: {}", id);

		CountryDto countryObj = modelMapper.map(country, CountryDto.class);
		
		log.debug("Updated Country DTO: {}", countryObj);
		
		return new ApiResponseDto<CountryDto>(true,"data has been updated",countryObj);
	}

//	delete country
	
public ApiResponseDto<String> deleteData(int id) {
		
		Country country= countryDataRepository.findById(id).orElseThrow(() -> {
			log.error("country not found");
			return new ResourceNotFoundException("country not found");
		});
		
			try {
			countryDataRepository.delete(country);
			}catch (Exception e) {
				log.error("Cannot delete or update a parent row: a foreign key constraint exception");
				throw new ForeignkeyException("Cannot delete or update "+country.getCountryName()+": a foreign key constraint exception kindly remove mapping first.");
			}
			
			log.info("Country deleted successfully with id: {}", id);
		
		return new ApiResponseDto<String>(true,"Country deleted successfully",null);
	}
	
	
	
}