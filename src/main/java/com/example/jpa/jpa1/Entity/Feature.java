package com.example.jpa.jpa1.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feature {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int featureId;
	private String featureName;
	private String description;
	private String category;
	

}
