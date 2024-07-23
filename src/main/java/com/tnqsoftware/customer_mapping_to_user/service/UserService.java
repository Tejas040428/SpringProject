package com.tnqsoftware.customer_mapping_to_user.service;

import java.util.Collection;
import java.util.List;

import com.tnqsoftware.customer_mapping_to_user.entity.AppUser;
import com.tnqsoftware.customer_mapping_to_user.entity.Customer;
import com.tnqsoftware.customer_mapping_to_user.entity.Role;
import com.tnqsoftware.customer_mapping_to_user.requestEntity.RequestCustomerMapping;
import com.tnqsoftware.customer_mapping_to_user.requestEntity.RequestSignUp;

public interface UserService {

	AppUser saveUser(AppUser appUser);
	AppUser changePassword(String oldPassword,String newPassword);
	AppUser getUser(String username);
	RequestCustomerMapping mapCustomer(RequestCustomerMapping mapping);
	List<RequestCustomerMapping> getAllListCustomerMappingRequest();
	RequestCustomerMapping getCustomerMappingRequest();
	Role getRoleFromName(String name);
	Collection<Customer> getMappedCustomer();
	public List<String> getUnMappedCustomer();
	
}
