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
	
	public static Specification<ApplyMapping> filter(Feature feature,Vehicle vehicle, Country country){
		
		return (root,query,cb) -> {
//			root.fetch("feature");			//for solving the n+1 problem
//		    root.fetch("vehicle");			//for solving the n+1 problem
//		    root.fetch("country");			//for solving the n+1 problem
//		
			List<Predicate> predicates = new ArrayList<>();
			
			if(feature != null) {
				predicates.add(cb.equal(root.get("feature"), feature));
			}
			if(vehicle != null) {
                predicates.add(cb.equal(root.get("vehicle"), vehicle));
			}
			if (country != null) {
                predicates.add(cb.equal(root.get("country"), country));
            }

			return cb.and(predicates.toArray(new Predicate[0]));
		};
		
	}

}
