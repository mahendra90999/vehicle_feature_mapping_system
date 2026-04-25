package com.example.jpa.jpa1.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class MappingDto {
	
	@NotNull
	@JsonProperty("feature_id")
	private int featureId;
	@NotNull
	@JsonProperty("vehicle_id")
	private int vehicleId;
	@NotNull
	@JsonProperty("country_id")
	private int countryId;
	@NotBlank
	private String status;
}
