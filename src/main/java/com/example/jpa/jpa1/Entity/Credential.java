package com.example.jpa.jpa1.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
		name="cred_seq",
		sequenceName = "cred_sequence",
		initialValue = 10000,
		allocationSize = 1)
public class Credential {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "cred_seq")
	private Long id;
	@Column(nullable = false, unique = true) 
	private String username;
	private String password;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
}
