package com.tnqsoftware.customer_mapping_to_user.requestEntity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @AllArgsConstructor 
@NoArgsConstructor @Data
public class RequestSignUp {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotEmpty
	private String name;
	@NotEmpty
	@Pattern(regexp="^[a-zA-Z0-9]{5,10}$") 
	@Size(min = 5, max = 10)
	private String username;
	@NotEmpty
	private String password;
	private Date createdDate;
	private Date updatedDate;
	

	
}
