package com.tnqsoftware.customer_mapping_to_user.entity;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter

@Getter

@AllArgsConstructor

@NoArgsConstructor

@ToString
public class UserCustomerMapping{
	private String username;
	private	Long[] customerId;
}
