package com.tnqsoftware.customer_mapping_to_user.service;

import java.util.List;

import com.tnqsoftware.customer_mapping_to_user.entity.AppUser;
import com.tnqsoftware.customer_mapping_to_user.entity.Customer;
import com.tnqsoftware.customer_mapping_to_user.entity.Role;
import com.tnqsoftware.customer_mapping_to_user.requestEntity.RequestCustomerMapping;
import com.tnqsoftware.customer_mapping_to_user.requestEntity.RequestSignUp;

public interface AdminService {
	AppUser saveUser(AppUser appUser);
	Role saveRole(Role role);
	Customer saveCustomer(Customer customer);
	Role addRoletoUser(String username,String roleName);
	AppUser getUser(String username);
	List<RequestSignUp> getRequestedUsers();
	RequestSignUp getRequestUser(String username);
	boolean removeRequestedSignUp(RequestSignUp requestSignUp);
	Role getRoleFromName(String name);
	void addCustomertoUser(RequestCustomerMapping customerMapping);
	void deleteCustomerMapping(RequestCustomerMapping customerMapping);
	List<Customer> listCustomer();
	public Customer checkIFCustomerPresent(String custname);
	public boolean activateOrDeactivateTheCustomer(String customerName);
	public List unmappedCustomerForUser(String username,List<String> customerName);
	public List<String> getAllUsers();
	public List<String> getMappedCustomer(String username);
}