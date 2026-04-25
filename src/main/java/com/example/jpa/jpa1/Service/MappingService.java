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
import com.example.jpa.jpa1.Dto.VehicleDto;
import com.example.jpa.jpa1.Entity.ApplyMapping;
import com.example.jpa.jpa1.Entity.Country;
import com.example.jpa.jpa1.Entity.Feature;
import com.example.jpa.jpa1.Entity.Vehicle;
import com.example.jpa.jpa1.Exception.DuplicateMappingException;
import com.example.jpa.jpa1.Exception.MappingNotFoundException;
import com.example.jpa.jpa1.Exception.ResourceNotFoundException;
import com.example.jpa.jpa1.Exception.ForeignkeyException.ForeignkeyException;
import com.example.jpa.jpa1.Repository.CountryDataRepository;
import com.example.jpa.jpa1.Repository.FeatureDataRepository;
import com.example.jpa.jpa1.Repository.MappingRepository;
import com.example.jpa.jpa1.Repository.VehicleDataRepository;
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
                 mappingDto.getVehicleId(), mappingDto.getFeatureId(), mappingDto.getCountryId());
        
        try {
            Vehicle vehicle = vehicleDataRepository.findById(mappingDto.getVehicleId())
                .orElseThrow(() -> {
                    log.error("Vehicle not found with ID: {}", mappingDto.getVehicleId());
                    return new RuntimeException("Vehicle not found");
                });
            
            Feature feature = featureDataRepository.findById(mappingDto.getFeatureId())
                .orElseThrow(() -> {
                    log.error("Feature not found with ID: {}", mappingDto.getFeatureId());
                    return new RuntimeException("Feature not found");
                });
            
            Country country = countryDataRepository.findById(mappingDto.getCountryId())
                .orElseThrow(() -> {
                    log.error("Country not found with ID: {}", mappingDto.getCountryId());
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

            Sort sort = direction.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() :
                    Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            log.debug("Pageable configured: Page {}, Size {}, Sort by {} {}", page, size, sortBy, direction);

            Page<ApplyMapping> mappings = mappingRepository.findAll(
                    MappingSpecification.filter(infoDto.getFeature_name(),infoDto.getVehicle_name(), infoDto.getCountry_name()), pageable);
            
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
	
	
//	update mapping
	public ApiResponseDto<MappingDto> updateData(int id, MappingDto dto) {
		log.info("Updating mapping with id: {}", id);
		
		ApplyMapping mapping = mappingRepository.findById(id).orElseThrow(() -> {
							log.error("mapping not found");
							return new RuntimeException("mapping not found");
						});

	    Vehicle vehicle = vehicleDataRepository.findById(dto.getVehicleId())
	            .orElseThrow(() -> new RuntimeException("Vehicle not found"));

	    Feature feature = featureDataRepository.findById(dto.getFeatureId())
	            .orElseThrow(() -> new RuntimeException("Feature not found"));

	    Country country = countryDataRepository.findById(dto.getCountryId())
	            .orElseThrow(() -> new RuntimeException("Country not found"));

		
		log.debug("Old Mapping Data: {}", mapping);
		
		mapping.setVehicle(vehicle);
		mapping.setFeature(feature);
		mapping.setCountry(country);
		mapping.setStatus(dto.getStatus());
		mappingRepository.save(mapping);
		
		log.info("mapping updated successfully with id: {}", id);

		MappingDto mappingobj = modelMapper.map(mapping, MappingDto.class);
		
		log.debug("Updated mapping DTO: {}", mappingobj);
		
		return new ApiResponseDto<MappingDto>(true,"data has been updated",mappingobj);
	}
	
	
	public ApiResponseDto<String> deleteData(int id) {
		
		ApplyMapping mapping = mappingRepository.findById(id).orElseThrow(() -> {
			log.error("mapping not found");
			return new RuntimeException("mapping not found");
		});
		
			try {
			mappingRepository.delete(mapping);
			}catch (Exception e) {
				log.error("Cannot delete or update a parent row: a foreign key constraint exception");
				throw new ForeignkeyException("Cannot delete or update "+mapping.getMappingId()+": a foreign key constraint exception kindly remove mapping first.");
			}
			
			log.info("Mapping deleted successfully with id: {}", id);
		
		return new ApiResponseDto<String>(true,"Mapping deleted successfully",null);
	}
	
	

}
