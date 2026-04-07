package com.example.jpa.jpa1.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger log = LoggerFactory.getLogger(MappingService.class);

	@CacheEvict(value = "mappingCache", allEntries = true)
	public ApiResponseDto<MappingDto> addData(MappingDto mappingDto) {
		 log.info("Adding mapping - Vehicle ID: {}, Feature ID: {}, Country ID: {}", 
                 mappingDto.getVehicle_id(), mappingDto.getFeature_id(), mappingDto.getCountry_id());
        
        try {
            Vehicle vehicle = vehicleDataRepository.findById(mappingDto.getVehicle_id())
                .orElseThrow(() -> {
                    log.error("Vehicle not found with ID: {}", mappingDto.getVehicle_id());
                    return new RuntimeException("Vehicle not found");
                });
            
            Feature feature = featureDataRepository.findById(mappingDto.getFeature_id())
                .orElseThrow(() -> {
                    log.error("Feature not found with ID: {}", mappingDto.getFeature_id());
                    return new RuntimeException("Feature not found");
                });
            
            Country country = countryDataRepository.findById(mappingDto.getCountry_id())
                .orElseThrow(() -> {
                    log.error("Country not found with ID: {}", mappingDto.getCountry_id());
                    return new RuntimeException("Country not found");
                });

            boolean exists = mappingRepository.existsByFeatureAndVehicleAndCountry(feature, vehicle, country);

            if (exists) {
                log.warn("Duplicate mapping detected for Vehicle: {}, Feature: {}, Country: {}", 
                         vehicle.getVehicleName(), feature.getFeatureName(), country.getCountryName());
                throw new DuplicateMappingException("Mapping already exists for given Vehicle, Feature and Country");
            }

            ApplyMapping mapping = ApplyMapping.builder()
                .vehicle(vehicle)
                .feature(feature)
                .country(country)
                .status(mappingDto.getStatus())
                .build();

            ApplyMapping mappingObj = mappingRepository.save(mapping);
            log.info("Mapping saved successfully");

            MappingDto mappingDtoObj = modelMapper.map(mappingObj, MappingDto.class);

            return new ApiResponseDto<>(true, "Mapping saved", mappingDtoObj);
        } catch (Exception e) {
            log.error("Failed to add mapping: {}", e.getMessage(), e);
            throw e;
        }
	}


	@Cacheable(value = "mappingCache",
	           key = "#infoDto.vehicle_name + '-' + #infoDto.feature_name + '-' + #infoDto.country_name + '-' + #page + '-' + #size")
	public ApiResponseDto<PageResponse<MappingStringDto>> getData(InfoDto infoDto,int page, int size,String sortBy, String direction){
		 log.info("Fetching mappings with filters - Vehicle: {}, Feature: {}, Country: {}, Page: {}, Size: {}, SortBy: {}, Direction: {}", 
                 infoDto.getVehicle_name(), infoDto.getFeature_name(), infoDto.getCountry_name(), page, size, sortBy, direction);
        
        try {
            Vehicle vehicle = null;
            Feature feature = null;
            Country country = null;
            
            if (infoDto.getVehicle_name() != null && !infoDto.getVehicle_name().isBlank()) {
                vehicle = vehicleDataRepository.findByVehicleName(infoDto.getVehicle_name())
                        .orElseThrow(() -> {
                            log.error("Vehicle not found: {}", infoDto.getVehicle_name());
                            return new ResourceNotFoundException("Vehicle not found " + infoDto.getVehicle_name());
                        });
                log.debug("Vehicle found: {}", vehicle.getVehicleName());
            }
            
            if (infoDto.getFeature_name() != null && !infoDto.getFeature_name().isBlank()) {
                feature = featureDataRepository.findByFeatureName(infoDto.getFeature_name())
                        .orElseThrow(() -> {
                            log.error("Feature not found: {}", infoDto.getFeature_name());
                            return new ResourceNotFoundException("Feature not found " + infoDto.getFeature_name());
                        });
                log.debug("Feature found: {}", feature.getFeatureName());
            }
            
            if (infoDto.getCountry_name() != null && !infoDto.getCountry_name().isBlank()) {
                country = countryDataRepository.findByCountryName(infoDto.getCountry_name())
                        .orElseThrow(() -> {
                            log.error("Country not found: {}", infoDto.getCountry_name());
                            return new ResourceNotFoundException("Country not found " + infoDto.getCountry_name());
                        });
                log.debug("Country found: {}", country.getCountryName());
            }
            
            Sort sort = direction.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() :
                    Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            log.debug("Pageable configured: Page {}, Size {}, Sort by {} {}", page, size, sortBy, direction);

            Page<ApplyMapping> mappings = mappingRepository.findAll(
                    MappingSpecification.filter(feature, vehicle, country), pageable);
            
            log.info("Found {} mappings matching the criteria", mappings.getTotalElements());

            if (mappings.isEmpty()) {
                log.warn("No mappings found for the given filters");
                throw new MappingNotFoundException("mappings not found for given filters");
            }
            
            Page<MappingStringDto> result = mappings.map(m -> {
                MappingStringDto dto = new MappingStringDto();
                dto.setVehicle_name(m.getVehicle().getVehicleName());
                dto.setFeature_name(m.getFeature().getFeatureName());
                dto.setCountry_name(m.getCountry().getCountryName());
                dto.setStatus(m.getStatus());
                return dto;
            });

            PageResponse<MappingStringDto> pageResponse = new PageResponse<>(
                    result.getContent(),
                    result.getNumber(),
                    result.getSize(),
                    result.getTotalElements(),
                    result.getTotalPages()
            );

            log.info("Successfully retrieved {} mappings", pageResponse.getTotalElements());
            return new ApiResponseDto<>(true, "Mapping Found", pageResponse);
        } catch (Exception e) {
            log.error("Failed to fetch mappings: {}", e.getMessage(), e);
            throw e;
        }

	}
	
	

}
