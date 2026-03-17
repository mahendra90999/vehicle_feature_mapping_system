package com.example.jpa.jpa1.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

	@Autowired
	MappingService mappingService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public MappingDto addData(@RequestBody MappingDto mappingDto) {	
		return mappingService.addData(mappingDto);
	}
	
	
	@PostMapping
	@Operation(summary = "Get mapping data", description = "Returns mapping data with filters")
	public ApiResponseDto<PageResponse<MappingStringDto>> getData(@RequestBody InfoDto infoDto,@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size,@RequestParam(defaultValue = "mappingId") String sortBy,
	        @RequestParam(defaultValue = "desc") String direction) {	
		
		return mappingService.getData(infoDto, page, size, sortBy, direction);
	}
	
}
