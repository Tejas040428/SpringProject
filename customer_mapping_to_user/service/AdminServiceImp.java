package com.tnqsoftware.customer_mapping_to_user.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tnqsoftware.customer_mapping_to_user.entity.AppUser;
import com.tnqsoftware.customer_mapping_to_user.entity.Customer;
import com.tnqsoftware.customer_mapping_to_user.entity.Role;
import com.tnqsoftware.customer_mapping_to_user.exception.CustomerIsNotMappedException;
import com.tnqsoftware.customer_mapping_to_user.exception.CustomerNotAddedException;
import com.tnqsoftware.customer_mapping_to_user.exception.UserNameNotFoundException;
import com.tnqsoftware.customer_mapping_to_user.repo.CustomerRepo;
import com.tnqsoftware.customer_mapping_to_user.repo.RequestCustomerMappingRepo;
import com.tnqsoftware.customer_mapping_to_user.repo.RoleRepo;
import com.tnqsoftware.customer_mapping_to_user.repo.SignUpRepo;
import com.tnqsoftware.customer_mapping_to_user.repo.UserRepo;
import com.tnqsoftware.customer_mapping_to_user.requestEntity.RequestCustomerMapping;
import com.tnqsoftware.customer_mapping_to_user.requestEntity.RequestSignUp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AdminServiceImp  implements AdminService{

	@Autowired
	private  SignUpRepo poxySignUp;
	@Autowired
	private  UserRepo repoUser;
	@Autowired
	private  RoleRepo repoRole;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private CustomerRepo customerRepo;
	@Autowired
	private RequestCustomerMappingRepo repo;
	 
	@Override
	public AppUser saveUser(AppUser appUser) {
		// TODO Auto-generated method stub
		log.info("Saving user in Admin service");
		return repoUser.save(appUser);
	}

	@Override
	public Role saveRole(Role role) {
		// TODO Auto-generated method stub
		log.info("Saving the role in Admin service ");
		return repoRole.save(role);
	}

	@Override
	public  Role addRoletoUser(String username, String roleName) {
		// TODO Auto-generated method stub
		log.info("Adding role to user in Admin service ");
		Set<AppUser> appUsers =  new HashSet<>();
		AppUser appUser = repoUser.findByUsername(username);
		if(appUser==null) {
			throw new UserNameNotFoundException();
		}
		appUsers.add(appUser);
		Role role = repoRole.findByName(roleName);
		role.getAppUsers().add(appUser);
		return repoRole.save(role);
	}

	@Override
	public AppUser getUser(String username) {
		// TODO Auto-generated method stub
		log.info("Fetching the user in Admin service ");
		return repoUser.findByUsername(username);
	}

	@Override
	public List<RequestSignUp> getRequestedUsers() {
		// TODO Auto-generated method stub
		log.info("fetching the all requested user");
		return poxySignUp.findAll();
	}
	
	@Override
	public RequestSignUp getRequestUser(String username) {
		// TODO Auto-generated method stub
		log.info("Fetching the user in User service ");
		return poxySignUp.findByUsername(username);
	}

	@Override
	public boolean removeRequestedSignUp(RequestSignUp requestSignUp) {
		// TODO Auto-generated method stub
		poxySignUp.delete(requestSignUp);
		return true;
	}

	@Override
	public Role getRoleFromName(String name) {
		// TODO Auto-generated method stub
		return repoRole.findByName(name);
	}

	@Override
	public void addCustomertoUser(RequestCustomerMapping customerMapping) {
		// TODO Auto-generated method stub
		AppUser appUser = repoUser.findByUsername(customerMapping.getUsername());
//		Set<Customer> customers = appUser.getCustomers();
//		for(String customerName  :customerMapping.getCustName()) {
//			
//			log.info("Adding Customer {} ",customerName);
//			customers.add(customerRepo.findByCustomerName(customerName));
//		}
//		appUser.setCustomers(customers);
//		repoUser.saveAndFlush(appUser);
		Set<AppUser> setOfAppUser  =  new HashSet<>();
		setOfAppUser.add(appUser);
		Collection<Customer> customers = new ArrayList<>();
		for(String custName : customerMapping.getCustName()) {
			Customer findByCustomerName = customerRepo.findByCustomerName(custName);
			findByCustomerName.getAppUsers().add(appUser);
			customerRepo.save(findByCustomerName);			
			log.info("added customer {}",customers);	
		}
		deleteCustomerMapping(repo.findByUsername(customerMapping.getUsername()));
		log.info("successfully removed");
	}

	@Override
	public void deleteCustomerMapping(RequestCustomerMapping customerMapping) {
		// TODO Auto-generated method stub
		repo.delete(customerMapping);
	}

	@Override
	public List<Customer> listCustomer() {
		// TODO Auto-generated method stub
		return customerRepo.findAll();
	}

	@Override
	public Customer saveCustomer(Customer customer) {
		// TODO Auto-generated method stub
		log.info("Inside the save Customer services");
		customer = customerRepo.save(customer);
		if(customer==null) {
			throw new CustomerNotAddedException();
		}
		return customer;
		 
	}
	@Override
	public Customer checkIFCustomerPresent(String custname) {
		return customerRepo.findByCustomerName(custname);
	}

	@Override
	public boolean activateOrDeactivateTheCustomer(String customerName) {
		// TODO Auto-generated method stub
		log.info("Inside the activateOrDeactivateTheCustomer");
		Customer customer = customerRepo.findByCustomerName(customerName);
		customer.setIsActive(customer.getIsActive()==1?0:1);
		if(customerRepo.save(customer)==null) {
			log.error("Is active is not updated ");
			return false;
		}
		else {
			log.info("Updated the is active");
			return true;
		}
	}

	public List unmappedCustomerForUser(String username, List<String> customerNames) {
		// TODO Auto-generated method stub
		
		log.info("Inside the service unmapped customer method {}",customerNames.size());
		ArrayList<String> customerNotAdded = new ArrayList<String>();
		AppUser user = repoUser.findByUsername(username);
		for (String name : customerNames) {
			Customer customer = customerRepo.findByCustomerName(name);
			if(customer==null) {
				customerNotAdded.add(name);
			}
			log.info("customer name {}",customer.getAppUsers().size());
//			Set<AppUser> collect = customer.getAppUsers().stream().filter(x->!x.getName().equals(username)).collect(Collectors.toSet());
			Set<AppUser> appUsers = customer.getAppUsers();
			appUsers.remove(user);
			log.info("customer name {}",appUsers.size());
//			customers.stream().map(x->x.getAppUsers().stream().filter(y->y.getUsername().equals(username)).collect(Collectors.toSet()));
//			log.info("{}",appUsers.size());
//			appUsers.remove(user);
//			customer.setAppUsers(appUsers);
			log.info("{}",customer.getAppUsers().size());
			repoUser.save(user);
			
			
		}
		if(customerNotAdded.size()>0) {
			return customerNotAdded;
		}
		return null;
	}

	@Override
	public List<String> getAllUsers() {
		// TODO Auto-generated method stub
		log.info("Inside the Get All users");
		return repoUser.findAll().stream().map(x->x.getUsername()).collect(Collectors.toList());
	}
	@Override
	public List<String> getMappedCustomer(String username) {
		// TODO Auto-generated method stub
		log.info("Inside get Mapped Service ");
		return repoUser.findByUsername(username).getCustomers().stream().map(x->x.getCustomerName()).collect(Collectors.toList());
	}
}
