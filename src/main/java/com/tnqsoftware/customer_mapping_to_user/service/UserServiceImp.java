package com.tnqsoftware.customer_mapping_to_user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tnqsoftware.customer_mapping_to_user.entity.AppUser;
import com.tnqsoftware.customer_mapping_to_user.entity.Customer;
import com.tnqsoftware.customer_mapping_to_user.entity.Role;
import com.tnqsoftware.customer_mapping_to_user.exception.CustomerMappingAlreadyDone;
import com.tnqsoftware.customer_mapping_to_user.exception.CustomerNotFoundException;
import com.tnqsoftware.customer_mapping_to_user.exception.OldPasswordWrongException;
import com.tnqsoftware.customer_mapping_to_user.exception.UsernameAlreadyFoundException;
import com.tnqsoftware.customer_mapping_to_user.repo.CustomerRepo;
import com.tnqsoftware.customer_mapping_to_user.repo.RequestCustomerMappingRepo;
import com.tnqsoftware.customer_mapping_to_user.repo.RoleRepo;
import com.tnqsoftware.customer_mapping_to_user.repo.UserRepo;
import com.tnqsoftware.customer_mapping_to_user.requestEntity.RequestCustomerMapping;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@Service
public class UserServiceImp implements UserService, UserDetailsService {

	@Autowired
	private UserRepo repoUser;
	@Autowired
	private RoleRepo repoRole;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private RequestCustomerMappingRepo repomapping;
	@Autowired
	private CustomerRepo customerRepo;

	private String getLoggedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		return username;
	}

	@Override
	public AppUser saveUser(AppUser appUser) {
		// TODO Auto-generated method stub
		if (repoUser.findByUsername(appUser.getUsername()) == null) {
			log.info("Saving user in User service");
			appUser.setPassword(encoder.encode(appUser.getPassword()));
			Set<Role> list = new HashSet<>();
			Role roleFromName = getRoleFromName("user");
			roleFromName.getAppUsers().add(appUser);
			list.add(roleFromName);
			appUser.setRoles(list);
			repoUser.save(appUser);
			return repoUser.findByUsername(appUser.getUsername());
		}
		throw new UsernameAlreadyFoundException();
	}

	@Override
	public AppUser getUser(String username) {
		// TODO Auto-generated method stub
		log.info("Fetching the user in User service ");
		return repoUser.findByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		// TODO Auto-generated method stub
		AppUser appUser = repoUser.findByUsername(username);
		if (appUser == null) {
			log.error("User Name not Found");
			throw new UsernameNotFoundException("user name is wrong : " + username);
		} else {
			log.info("user found");
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		appUser.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return new User(username, appUser.getPassword(), authorities);
	}

	@Override
	public RequestCustomerMapping mapCustomer(RequestCustomerMapping mapping) {
		// TODO Auto-generated method stub
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		log.info("Inside mapping");
		for (String custName : mapping.getCustName()) {
			if (customerRepo.findByCustomerName(custName) == null) {
				throw new CustomerNotFoundException("Customer not Found");
			}
			AppUser findByUsername = repoUser.findByUsername(username);
			if (findByUsername.getCustomers().contains(customerRepo.findByCustomerName(custName))) {
				throw new CustomerMappingAlreadyDone("Reuested Customer already mapped");
			}
		}
		RequestCustomerMapping mapperUser = repomapping.findByUsername(username);
		if (mapperUser != null) {
			List<String> custNames = mapperUser.getCustName();
			custNames.addAll(mapping.getCustName());
			mapperUser.setCustName(custNames);
			return repomapping.save(mapperUser);
		}
		mapping.setUsername(username);
		RequestCustomerMapping save = repomapping.save(mapping);
		if (save == null) {
			throw new RuntimeException("Something went wrong");
		}
		return save;

	}

	private List<Role> getRoleWithUser() {

		Role byId = repoRole.getById((long) 2);
		ArrayList<Role> roles = new ArrayList<>();
		roles.add(byId);
		return roles;
	}

	@Override
	public List<RequestCustomerMapping> getAllListCustomerMappingRequest() {
		// TODO Auto-generated method stub
		return repomapping.findAll();
	}

	@Override
	public Role getRoleFromName(String name) {
		// TODO Auto-generated method stub
		return repoRole.findByName(name);
	}

	@Override
	public Collection<Customer> getMappedCustomer() {
		// TODO Auto-generated method stub
		log.info("Inside the get MappedCustomer {} ", getLoggedUser());
		return repoUser.findByUsername(getLoggedUser()).getCustomers();
	}

	@Override
	public List<String> getUnMappedCustomer() {
		// TODO Auto-generated method stub
		log.info("Inside Service : getUnMappedCustomer method");
		List<String> listOfCustomer = new ArrayList<>();
		List<Object[]> allCustomer = customerRepo.getAllCustomer(repoUser.findByUsername(getLoggedUser()).getId());
		for (Object[] dataOfCustomers : allCustomer) {
			for (int i = 0; i < dataOfCustomers.length; i++) {
				listOfCustomer.add(((String) dataOfCustomers[i]));
			}
		}
		RequestCustomerMapping custNames = repomapping.findByUsername(getLoggedUser());
		if (custNames != null) {
			return listOfCustomer.stream().filter(x -> (custNames.getCustName().contains(x)) ? false : true)
					.collect(Collectors.toList());
		}
		else{
			return listOfCustomer;
		}
	}

	@Override
	public RequestCustomerMapping getCustomerMappingRequest() {
		// TODO Auto-generated method stub
		return repomapping.findByUsername(getLoggedUser());
	}

	@Override
	public AppUser changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		AppUser user = repoUser.findByUsername(getLoggedUser());
		if (user.getPassword().equals(oldPassword)) {
			user.setPassword(newPassword);
			user.setUpdatedDate(new Date());
			if (repoUser.save(user) != null) {
				return user;
			}
			throw new RuntimeException("Somethimg went wrong");
		}
		throw new OldPasswordWrongException();
	}

}
