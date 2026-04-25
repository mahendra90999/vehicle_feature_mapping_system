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
import com.example.jpa.jpa1.Dto.FeatureDto;
import com.example.jpa.jpa1.Dto.FeatureIdDto;
import com.example.jpa.jpa1.Entity.Feature;
import com.example.jpa.jpa1.Exception.ForeignkeyException.ForeignkeyException;
import com.example.jpa.jpa1.Repository.FeatureDataRepository;

import jakarta.validation.Valid;

@Service
public class FeatureService {
	
	@Autowired
	FeatureDataRepository featureDataRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	private final Logger log = LoggerFactory.getLogger(FeatureService.class);

	public FeatureDto addData(FeatureDto featureDto) {
		
		Feature feature = Feature.builder()
							.featureName(featureDto.getFeatureName())
							.category(featureDto.getCategory())
							.description(featureDto.getDescription())
							.build();
		
		Feature saveFeature = featureDataRepository.save(feature);
		
		FeatureDto featureobj = modelMapper.map(saveFeature, FeatureDto.class);
		
		return featureobj;
	}
	
	
//	for searching data
	public ApiResponseDto<?> showData(String name,int page) {
		log.info("showing all data");
			
			Pageable pageable = PageRequest.of(page, 10);
			
			Page<Feature> featurePage =
		            featureDataRepository.findByFeatureNameContainingIgnoreCase(name, pageable);
			
			log.info("Total records found: {}", featurePage.getTotalElements());
			
			List<FeatureIdDto> listDto = featurePage.getContent().stream()
											.map(d -> modelMapper.map(d,FeatureIdDto.class))
											.toList();
		
		return new ApiResponseDto<>(true,"all feature data",listDto);
	}

	
	
	

	
//	for update feature data
	public ApiResponseDto<FeatureDto> updateData(int id, @Valid FeatureDto dto) {
		
		Feature feature = featureDataRepository.findById(id).orElseThrow(() -> {
			log.error("feature not found");
			return new RuntimeException("feature not found");
		});
		
		
		log.debug("old Feature Data: ",feature);
		
		feature.setFeatureName(dto.getFeatureName());
		feature.setDescription(dto.getDescription());
		feature.setCategory(dto.getCategory());
		featureDataRepository.save(feature);

		log.info("feature updated successfully with id: {}", id);

		FeatureDto featureobj = modelMapper.map(feature, FeatureDto.class);
		
		log.debug("Updated Vehicle DTO: {}", featureobj);
		
		return new ApiResponseDto<FeatureDto>(true,"data has been updated",featureobj);

	}
	
	
	
//	for delete data
public ApiResponseDto<String> deleteData(int id) {
		
		Feature feature = featureDataRepository.findById(id).orElseThrow(() -> {
			log.error("feature not found");
			return new RuntimeException("feature not found");
		});
		
			try {
			featureDataRepository.delete(feature);
			}catch (Exception e) {
				log.error("Cannot delete or update a parent row: a foreign key constraint exception");
				throw new ForeignkeyException("Cannot delete or update "+feature.getFeatureName()+": a foreign key constraint exception kindly remove mapping first.");
			}
			
			log.info("feature deleted successfully with id: {}", id);
		
		return new ApiResponseDto<String>(true,"feature deleted successfully",null);
	}
	
	
}
