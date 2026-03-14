package com.example.jpa.jpa1.Dto;

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
	
	private int feature_id;
	private int vehicle_id;
	private int country_id;
	private String status;
}
