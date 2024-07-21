package com.tnqsoftware.customer_mapping_to_user.controller;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnqsoftware.customer_mapping_to_user.entity.Customer;
import com.tnqsoftware.customer_mapping_to_user.entity.Role;
import com.tnqsoftware.customer_mapping_to_user.exception.CustomerAlreadyFound;
import com.tnqsoftware.customer_mapping_to_user.exception.CustomerRequestNotFound;
import com.tnqsoftware.customer_mapping_to_user.exception.CustomerValueNotUpatedException;
import com.tnqsoftware.customer_mapping_to_user.requestEntity.RequestCustomerMapping;
import com.tnqsoftware.customer_mapping_to_user.requestEntity.RequestSignUp;
import com.tnqsoftware.customer_mapping_to_user.service.AdminService;
import com.tnqsoftware.customer_mapping_to_user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@Slf4j 
@CrossOrigin("*")
@Api(tags="Admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private UserService userservice;

	@ApiOperation(value = "API used to set the roles to Users")
	@PostMapping("/setRoles")
	public ResponseEntity<Object> setRoles(@RequestBody SetRoles setroles) {
		Role addRoletoUser = adminService.addRoletoUser(setroles.getUsername(), setroles.getRole());
		if (addRoletoUser != null) {
			return new ResponseEntity(HttpStatus.ACCEPTED);
		}
		throw new RuntimeException("Roles not set");
	}

	@ApiOperation(value = "Api for Sign Up Of a User")
	@GetMapping("/signUpRequest")
	public Map<String, String> requestForSignUpRequest() {
		// based on angular request\
		log.info("Inside Admin signUpRequests");
		List<RequestSignUp> requestedUsers = adminService.getRequestedUsers();
		Map<String, String> requested = new TreeMap<>();
		for (RequestSignUp requestSignUp : requestedUsers) {
			log.info("requested SignUp {}", requestSignUp.getName());
			requested.put(requestSignUp.getUsername(), requestSignUp.getName());
		}
		return requested;
	}

	/*
	 * @PostMapping("/signUpRequest") public ResponseEntity<Object>
	 * requestForSignUp(@RequestBody Map<String, String> userResponseBodies) { //
	 * based on angular request log.info("Inside POST save user"); Set<String>
	 * keySet = userResponseBodies.keySet(); for (String string : keySet) {
	 * log.info("add ing {} into db", string); RequestSignUp requestUser =
	 * adminService.getRequestUser(string); log.info("Requested",
	 * requestUser.getUsername()); AppUser appUser = new
	 * AppUser(requestUser.getId(), requestUser.getName(),
	 * requestUser.getUsername(), requestUser.getPassword(), new Date(), new
	 * Date()); ArrayList<Role> list = new ArrayList<>();
	 * list.add(adminService.getRoleFromName("user")); appUser.setRoles(list);
	 * adminService.saveUser(appUser);
	 * adminService.removeRequestedSignUp(requestUser); } return
	 * ResponseEntity.status(HttpStatus.CREATED).build(); }
	 */
	@ApiOperation("Api for adding the customer")
	@PostMapping("/addCustomer")
	public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {
		return new ResponseEntity(adminService.saveCustomer(customer), HttpStatus.ACCEPTED);
	}

	@ApiOperation("Api to find the customer by customer name")
	@PostMapping("/findCustomer")
	public ResponseEntity<CustomerName> findCustomer(@RequestBody CustomerName custName) {
		if (adminService.checkIFCustomerPresent(custName.getCustomer()) == null) {
			return ResponseEntity.ok(custName);
		}
		throw new CustomerAlreadyFound("Customer Already Found : " + custName.getCustomer());
	}
	@ApiOperation(value = "Api to get customer that are mapped")
	@GetMapping("/requestForCustomerMap")
	public List<RequestCustomerMapping> requestForCustomerMapping() {
		// based on angular request
		log.info("Inside Admin request mapping");
		List<RequestCustomerMapping> allListCustomerMappingRequest = userservice.getAllListCustomerMappingRequest();
		if (allListCustomerMappingRequest == null) {
			throw new CustomerRequestNotFound("No Customers Found");
		}
		return allListCustomerMappingRequest;
	}
	@ApiOperation(value = "Api to get All customer list")
	@GetMapping("/customerList")
	public List<Customer> requestForCustomerList() {
		// based on angular request
		log.info("Inside Admin request customers");
		return adminService.listCustomer();

	}
	@ApiOperation(value = "Request to map a customer with the user API")
	@PostMapping("/requestForCustomerMap")
	public ResponseEntity<Object> requestCustomerMapping(@RequestBody RequestCustomerMapping customerMapping) {
		// based on angular request

		log.info("Inside Admin request mapping");
		log.info("customer list{}", customerMapping);
		adminService.addCustomertoUser(customerMapping);

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}
	@ApiOperation(value = "Api to delete the requested mapping customers")
	@PostMapping("/discardForCustomerMap")
	public ResponseEntity<Object> deleteCustomerMapping(@RequestBody RequestCustomerMapping customerMapping) {
		// based on angular request

		log.info("Inside Admin request mapping");
		log.info("customer list{}", customerMapping);
		adminService.deleteCustomerMapping(customerMapping);

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}

	@ApiOperation(value = "Api to edit the customer status")
	@PutMapping("/editIsActive")
	public ResponseEntity<Object> activateOrDeactivateTheCustomer(@RequestBody CustomerName customerName) {
		log.info("Inside the editActive method");
		if (adminService.activateOrDeactivateTheCustomer(customerName.getCustomer())) {
			log.info("updated");
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} else {
			log.error("Not updated");
			throw new CustomerValueNotUpatedException("Is Active is not updated of customer : "+customerName.getCustomer());
		}
	}

	@ApiOperation(value = "Api to unmapp the customer with user")
	@PostMapping("/unmapCustomer")
	public ResponseEntity<Object> unMappingCustomerForUser(@RequestBody UnMappingCusotmer unmapping) {
		if(adminService.unmappedCustomerForUser(unmapping.getUsername(), unmapping.getCustomernames())==null) {
		Message message = new Message("Un Map successfull");
		return new ResponseEntity(message, HttpStatus.OK);
		}
		throw new RuntimeException();

	}
	
	@ApiOperation(value = "Api to get All usernames")
	@GetMapping("/getAllUsers")
	public List<String> getAllUaerNames() {		
		log.info("Inside the getAllUser controller");
		return adminService.getAllUsers();
	}
	
	@ApiOperation(value = "Api to get mapped customer by their username")
	@GetMapping("/getMappedCustomer/{username}")
	public List<String> getAllMappedCusomer(@PathVariable String username) {
		log.info("Inside the getMappedCustomer controller");
		return adminService.getMappedCustomer(username);
	}
	
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class UserRequestBody {
	private String userId;
	private List<Long> custList;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class JwtRequest {
	private String username;
	private String password;
}

@NoArgsConstructor
@AllArgsConstructor
@Data
class CustomerName {
	private String customer;

}

@NoArgsConstructor
@AllArgsConstructor
@Data
class SetRoles {
	private String username;
	private String role;

}

@NoArgsConstructor
@AllArgsConstructor
@Data
class UnMappingCusotmer {
	private String username;
	private List<String> customernames;
}

@NoArgsConstructor
@AllArgsConstructor
@Data
class Message {
	private String message;
}
