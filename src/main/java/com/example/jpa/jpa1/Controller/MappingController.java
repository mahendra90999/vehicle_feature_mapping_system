package com.example.jpa.jpa1.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.jpa1.Dto.ApiResponseDto;
import com.example.jpa.jpa1.Dto.InfoDto;
import com.example.jpa.jpa1.Dto.MappingDto;
import com.example.jpa.jpa1.Dto.MappingStringDto;
import com.example.jpa.jpa1.Dto.PageResponse;
import com.example.jpa.jpa1.Service.MappingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/mappings")
public class MappingController {

	private final MappingService mappingService;
    private static final Logger log = LoggerFactory.getLogger(MappingController.class);

	
	public MappingController(MappingService mappingService) {
		this.mappingService = mappingService;
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public ApiResponseDto<MappingDto> addData(@RequestBody MappingDto mappingDto) {	
		log.info("Adding new mapping - Vehicle: {}, Feature: {}, Country: {}", 
                mappingDto.getVehicle_id(), mappingDto.getFeature_id(), mappingDto.getCountry_id());
       try {
           ApiResponseDto<MappingDto> result = mappingService.addData(mappingDto);
           log.info("Mapping added successfully.. ");
           return result;
       } catch (Exception e) {
           log.error("Failed to add mapping: {}", e.getMessage(), e);
           throw e;
       }
	}
	
	
	@PostMapping
	@Operation(summary = "Get mapping data", description = "Returns mapping data with filters")
	public ApiResponseDto<PageResponse<MappingStringDto>> getData(@RequestBody InfoDto infoDto,@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size,@RequestParam(defaultValue = "mappingId") String sortBy,
	        @RequestParam(defaultValue = "desc") String direction) {	
		
		log.info("Fetching mappings - Vehicle: {}, Feature: {}, Country: {}, Page: {}, Size: {}", 
                infoDto.getVehicle_name(), infoDto.getFeature_name(), infoDto.getCountry_name(), page, size);
       try {
           ApiResponseDto<PageResponse<MappingStringDto>> result = 
               mappingService.getData(infoDto, page, size, sortBy, direction);
           log.debug("Successfully retrieved mappings");
           return result;
       } catch (Exception e) {
           log.error("Failed to retrieve mappings: {}", e.getMessage(), e);
           throw e;
       }
	}
	
}
