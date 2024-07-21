package com.tnqsoftware.customer_mapping_to_user.repo;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tnqsoftware.customer_mapping_to_user.entity.AppUser;

public interface UserRepo extends JpaRepository<AppUser, Long> {

	AppUser findByUsername(String username);
	@Query(value = "select * from customer where customer_id not in (select customer_id from user_customer where user_id =:#{#user.id})",nativeQuery = true)
	Collection<Object> getAllUnMappedCustomer(@Param("user") AppUser user);
}
