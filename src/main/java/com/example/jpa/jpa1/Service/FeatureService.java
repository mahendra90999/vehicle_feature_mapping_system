package com.example.jpa.jpa1.Service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Dto.FeatureDto;
import com.example.jpa.jpa1.Entity.Feature;
import com.example.jpa.jpa1.Repositoroy.FeatureDataRepository;

@Service
public class FeatureService {
	
	@Autowired
	FeatureDataRepository featureDataRepository;
	
	@Autowired
	ModelMapper modelMapper;

	public FeatureDto addData(FeatureDto featureDto) {
		
		Feature feature = Feature.builder()
							.featureName(featureDto.getFeature_name())
							.category(featureDto.getCategory())
							.description(featureDto.getDescription())
							.build();
		
		Feature saveFeature = featureDataRepository.save(feature);
		
		FeatureDto featureobj = modelMapper.map(saveFeature, FeatureDto.class);
		
		return featureobj;
	}
	
	
}
