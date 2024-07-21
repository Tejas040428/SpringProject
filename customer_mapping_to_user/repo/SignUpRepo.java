package com.tnqsoftware.customer_mapping_to_user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tnqsoftware.customer_mapping_to_user.requestEntity.RequestSignUp;

public interface SignUpRepo  extends JpaRepository<RequestSignUp, Long>{
	RequestSignUp findByUsername(String username);
}
