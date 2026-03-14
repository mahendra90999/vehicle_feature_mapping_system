package com.example.jpa.jpa1.Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Dto.ApiResponseDto;
import com.example.jpa.jpa1.Dto.InfoDto;
import com.example.jpa.jpa1.Dto.MappingDto;
import com.example.jpa.jpa1.Dto.MappingStringDto;
import com.example.jpa.jpa1.Dto.PageResponse;
import com.example.jpa.jpa1.Entity.ApplyMapping;
import com.example.jpa.jpa1.Entity.Country;
import com.example.jpa.jpa1.Entity.Feature;
import com.example.jpa.jpa1.Entity.Vehicle;
import com.example.jpa.jpa1.Exception.DuplicateMappingException;
import com.example.jpa.jpa1.Exception.MappingNotFoundException;
import com.example.jpa.jpa1.Exception.ResourceNotFoundException;
import com.example.jpa.jpa1.Repositoroy.CountryDataRepository;
import com.example.jpa.jpa1.Repositoroy.FeatureDataRepository;
import com.example.jpa.jpa1.Repositoroy.MappingRepository;
import com.example.jpa.jpa1.Repositoroy.VehicleDataRepository;
import com.example.jpa.jpa1.Specification.MappingSpecification;

@Service
public class MappingService {

	@Autowired
	VehicleDataRepository vehicleDataRepository;
	@Autowired
	FeatureDataRepository featureDataRepository;
	@Autowired
	CountryDataRepository countryDataRepository;
	@Autowired
	MappingRepository mappingRepository;
	

	@Autowired
	ModelMapper modelMapper;

	@CacheEvict(value = "mappingCache", allEntries = true)
	public MappingDto addData(MappingDto mappingDto) {

		Vehicle vehicle = vehicleDataRepository.findById(mappingDto.getVehicle_id()).orElseThrow();
		Feature feature = featureDataRepository.findById(mappingDto.getFeature_id()).orElseThrow();
		Country country = countryDataRepository.findById(mappingDto.getCountry_id()).orElseThrow();

		boolean exists = mappingRepository.existsByFeatureAndVehicleAndCountry(feature, vehicle, country);

		if (exists) {
			throw new DuplicateMappingException("Mapping already exists for given Vehicle, Feature and Country");
		}

		ApplyMapping mapping = ApplyMapping.builder().vehicle(vehicle).feature(feature).country(country)
				.status(mappingDto.getStatus()).build();

		ApplyMapping mappingobj = mappingRepository.save(mapping);

		MappingDto mappingDtoObje = modelMapper.map(mappingobj, MappingDto.class);

		return mappingDtoObje;
	}

//	public ApiResponseDto<MappingStringDto> getData(InfoDto infoDto) {
//
//		Vehicle vehicle = vehicleDataRepository.findByVehicleName(infoDto.getVehicle_name())
//				.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found " + infoDto.getVehicle_name()));
//		Feature feature = featureDataRepository.findByFeatureName(infoDto.getFeature_name())
//				.orElseThrow(() -> new ResourceNotFoundException("feature not found " + infoDto.getFeature_name()));
//		Country country = CountryDataRepository.findByCountryName(infoDto.getCountry_name())
//				.orElseThrow(() -> new ResourceNotFoundException("country not found " + infoDto.getCountry_name()));
//
//		ApplyMapping applyMapping = mappingRepository.findByFeatureAndVehicleAndCountry(feature, vehicle, country);
//
//		if (applyMapping == null) {
//	        throw new MappingNotFoundException(
//	                "Mapping not found for given Vehicle, Feature and Country");
//	    }
//
//		
//		modelMapper.typeMap(ApplyMapping.class, MappingStringDto.class).addMappings(mapper -> {
//			mapper.map(src -> src.getVehicle().getVehicleName(), MappingStringDto::setVehicle_name);
//			mapper.map(src -> src.getFeature().getFeatureName(), MappingStringDto::setFeature_name);
//			mapper.map(src -> src.getCountry().getCountryName(), MappingStringDto::setCountry_name);
//		});
//		
//		MappingStringDto result=modelMapper.map(applyMapping, MappingStringDto.class);
//		
//		return new ApiResponseDto<>(true,"Mapping Found",result);
//	}

	@Cacheable(value = "mappingCache",
	           key = "#infoDto.vehicle_name + '-' + #infoDto.feature_name + '-' + #infoDto.country_name + '-' + #page + '-' + #size")
	public ApiResponseDto<PageResponse<MappingStringDto>> getData(InfoDto infoDto,int page, int size,String sortBy, String direction){
		
		Vehicle vehicle = null;
		Feature feature = null;
		Country country = null;
		
		if(infoDto.getVehicle_name()!= null && !infoDto.getVehicle_name().isBlank()) {
			vehicle = vehicleDataRepository.findByVehicleName(infoDto.getVehicle_name())
					.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found " + infoDto.getVehicle_name()));
		}
		
		if(infoDto.getFeature_name() != null && !infoDto.getFeature_name().isBlank()) {
			feature = featureDataRepository.findByFeatureName(infoDto.getFeature_name())
					.orElseThrow(() -> new ResourceNotFoundException("Feature not found " + infoDto.getFeature_name()));
		}
		
		if(infoDto.getCountry_name() != null && !infoDto.getCountry_name().isBlank()) {
			country= countryDataRepository.findByCountryName(infoDto.getCountry_name())
					.orElseThrow(() -> new ResourceNotFoundException("Country not found " + infoDto.getCountry_name()));
			
		}
		
		
		Sort sort = direction.equalsIgnoreCase("desc") ?
		        Sort.by(sortBy).descending() :
		        Sort.by(sortBy).ascending();

		
		Pageable pageable = PageRequest.of(page, size, sort);


		Page<ApplyMapping> mappings = mappingRepository.findAll(
	            MappingSpecification.filter(feature, vehicle, country),pageable
			    );
		
		if(mappings.isEmpty()) {
			throw new MappingNotFoundException("mappings not found for given filters");
		}
		
		Page<MappingStringDto> result = mappings.map(m -> {
			MappingStringDto dto = new MappingStringDto();
			dto.setVehicle_name(m.getVehicle().getVehicleName());
	        dto.setFeature_name(m.getFeature().getFeatureName());
	        dto.setCountry_name(m.getCountry().getCountryName());
	        dto.setStatus(m.getStatus());
	        return dto;
		})	;	
		
		PageResponse<MappingStringDto> pageResponse =
	            new PageResponse<>(
	                    result.getContent(),
	                    result.getNumber(),
	                    result.getSize(),
	                    result.getTotalElements(),
	                    result.getTotalPages()
	            );

		
		
		return new ApiResponseDto<>(true, "Mapping Found", pageResponse);

	}
	
	

}
