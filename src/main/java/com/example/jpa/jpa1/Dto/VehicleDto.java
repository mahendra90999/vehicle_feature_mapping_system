package com.example.jpa.jpa1.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDto {
	
	@NotBlank(message = "Company name cannot be blank")
	private String company_name;
	@NotBlank(message = "Vehicle name cannot be blank")
	private String vehicle_name;
	@NotBlank(message = "variant name cannot be blank")
	private String variant;
	
}
