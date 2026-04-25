package com.example.jpa.jpa1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.jpa1.Dto.ApiResponseDto;
import com.example.jpa.jpa1.Dto.FeatureDto;
import com.example.jpa.jpa1.Service.FeatureService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/features")
public class FeatureController {

	@Autowired
	FeatureService featureService;
	
	@GetMapping("/{name}")
	public ApiResponseDto<?> showData(@PathVariable String name,@RequestParam(defaultValue = "0") int page){
		return featureService.showData(name,page);
	}

	
	@PostMapping("/add")
	public FeatureDto addData(@RequestBody FeatureDto featureDto) {
		return featureService.addData(featureDto);
	}
	
	@PutMapping("update/{id}")
	public ApiResponseDto<FeatureDto> updateData(@PathVariable int id,@Valid @RequestBody FeatureDto dto){
		return featureService.updateData(id,dto);
	}
	
	@DeleteMapping("delete/{id}")
	public ApiResponseDto<String> deleteData(@PathVariable int id){
		return featureService.deleteData(id);
	}
	
}
