package com.example.jpa.jpa1.Specification;


import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate; 

import org.springframework.data.jpa.domain.Specification;

import com.example.jpa.jpa1.Entity.ApplyMapping;
import com.example.jpa.jpa1.Entity.Country;
import com.example.jpa.jpa1.Entity.Feature;
import com.example.jpa.jpa1.Entity.Vehicle;


public class MappingSpecification {
	
	public static Specification<ApplyMapping> filter(String featureName,String vehicleName, String countryName){
		
		return (root,query,cb) -> {
//			root.fetch("feature");			//for solving the n+1 problem
//		    root.fetch("vehicle");			//for solving the n+1 problem
//		    root.fetch("country");			//for solving the n+1 problem
//		
			List<Predicate> predicates = new ArrayList<>();
			
			if(featureName != null && !featureName.isBlank()) {
				String value = featureName.trim().toLowerCase();
				predicates.add(cb.like(cb.lower(root.get("feature").get("featureName")),"%"+value+"%"));
			}
			if(vehicleName != null && !vehicleName.isBlank()) {
				String value = vehicleName.trim().toLowerCase();
                predicates.add(cb.like(cb.lower(root.get("vehicle").get("vehicleName")),"%"+value+"%"));
			}
			if (countryName != null && !countryName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("country").get("countryName")),"%"+countryName.trim().toLowerCase()+"%"));
            }

			return cb.and(predicates.toArray(new Predicate[0]));
		};
		
	}

}
