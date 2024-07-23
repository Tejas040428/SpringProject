package com.tnqsoftware.customer_mapping_to_user.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tnqsoftware.customer_mapping_to_user.entity.Customer;

public interface CustomerRepo  extends JpaRepository<Customer, Long>{

	Customer findByCustomerName(String customerName);
	@Query(value = "select customer_name from customer where customer_id not in (select customer_id from user_customer where user_id = :id)",nativeQuery = true)
	List<Object[]> getAllCustomer(@Param("id")Long id); 
}
