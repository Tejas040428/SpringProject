package com.tnqsoftware.customer_mapping_to_user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tnqsoftware.customer_mapping_to_user.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

	Role findByName(String name);
	
}
