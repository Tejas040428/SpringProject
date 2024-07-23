package com.tnqsoftware.customer_mapping_to_user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tnqsoftware.customer_mapping_to_user.requestEntity.RequestCustomerMapping;

public interface RequestCustomerMappingRepo extends JpaRepository<RequestCustomerMapping, String>{

	RequestCustomerMapping findByUsername(String username);
}
