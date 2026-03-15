package com.example.jpa.jpa1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.jpa1.Dto.FeatureDto;
import com.example.jpa.jpa1.Service.FeatureService;

@RestController
@RequestMapping("/features")
public class FeatureController {

	@Autowired
	FeatureService featureService;
	
	@PostMapping("/add")
	public FeatureDto addData(@RequestBody FeatureDto featureDto) {
		return featureService.addData(featureDto);
	}
}
