package com.example.jpa.jpa1.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDto {
	
	@NotBlank(message = "Company name cannot be blank")
	@JsonProperty("company_name")
	private String companyName;
	@NotBlank(message = "Vehicle name cannot be blank")
	@JsonProperty("vehicle_name")
	private String vehicleName;
	@NotBlank(message = "variant name cannot be blank")
	@JsonProperty("variant")
	private String variant;
	
}
