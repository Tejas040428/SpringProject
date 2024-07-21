package com.tnqsoftware.customer_mapping_to_user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tnqsoftware.customer_mapping_to_user.entity.Scope;

public interface ScopeRepo extends JpaRepository<Scope, Long>{
	Scope findByScopeName(String scopeName);
}
