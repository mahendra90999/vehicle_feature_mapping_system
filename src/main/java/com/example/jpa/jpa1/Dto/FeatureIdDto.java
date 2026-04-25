package com.example.jpa.jpa1.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeatureIdDto {
	private int id;
	private String featureName;
	private String description;
	private String category;


}
