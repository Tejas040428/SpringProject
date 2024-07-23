package com.tnqsoftware.customer_mapping_to_user.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnqsoftware.customer_mapping_to_user.entity.AppUser;
import com.tnqsoftware.customer_mapping_to_user.entity.Customer;
import com.tnqsoftware.customer_mapping_to_user.entity.Role;
import com.tnqsoftware.customer_mapping_to_user.exception.CustomerRequestNotFound;
import com.tnqsoftware.customer_mapping_to_user.exception.NoCustomerMappedException;
import com.tnqsoftware.customer_mapping_to_user.exception.UsernameAlreadyFoundException;
import com.tnqsoftware.customer_mapping_to_user.requestEntity.RequestCustomerMapping;
import com.tnqsoftware.customer_mapping_to_user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/service")
@Slf4j
@Api(tags = "User")
public class UserController {

	@Autowired
	private AdminController adminController;
	@Autowired
	private UserService service;

	@GetMapping("/hi")
	public String hi() {
		return "HI";
	}
	
	@ApiOperation(value = "Api to save user")
	@PostMapping("/save/user")
	public ResponseEntity<Object> saveUser(@RequestBody AppUser appUser,Role role) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
		appUser.setCreatedDate(new Date());
		appUser.setUpdatedDate(new Date());
		return ResponseEntity.created(uri).body(service.saveUser(appUser));
	}
	

	@ApiOperation(value = "Api for requesting the mapping of customer with user")
	@PostMapping("/user/customer/mapping")
	public ResponseEntity<Object> mapCustomer(@RequestBody RequestCustomerMapping  customerMapping){
		log.info("customer {} {} ",customerMapping.getUsername(),customerMapping.getCustName());
		if(service.mapCustomer(customerMapping)!=null) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@ApiOperation(value = "Api to check whether username is available of not")
	@PostMapping("/user/checkusername")
	public ResponseEntity<Object> usernameCheck(@RequestBody CheckUserName user){
		if(service.getUser(user.getUsername())==null) {
			log.info("username {} ", user);
			return ResponseEntity.ok(user);
		}
		else
			throw new UsernameAlreadyFoundException();
	}
	@ApiOperation(value = "Api to get mapped cutomer for a user")
	@GetMapping("/user/getMappedCustomer")
	public ResponseEntity<List<Customer>> getMappedCustomer(){
		Collection<Customer> mappedCustomer = service.getMappedCustomer();
		
		if(mappedCustomer.size()==0) {
		    throw new NoCustomerMappedException();
		}
		return new ResponseEntity(mappedCustomer,HttpStatus.OK);
		
	}
	@ApiOperation(value = "Api to get the requested mapped customers")
	@GetMapping("/user/requestForCustomerMap")
	public ResponseEntity<RequestCustomerMapping> requestForCustomerMapping(){
		//based on angular request
		log.info("Inside USer request mapping");
		 RequestCustomerMapping customerMappingRequest = service.getCustomerMappingRequest();
		 if(customerMappingRequest==null) {
			throw new CustomerRequestNotFound("No Customers Found"); 
		 }
		return new ResponseEntity<RequestCustomerMapping>(customerMappingRequest,HttpStatus.OK);
	}
	@ApiOperation(value = "Api to get the unmapped customers of a user")
	@GetMapping("/user/getUnMappedCustomer")
	public List<String> getUnMappedCustomer(){
		List<String> mappedCustomer = service.getUnMappedCustomer();
		if(mappedCustomer.size()==0) {
		    throw new NoCustomerMappedException();
		}
	 	return mappedCustomer;
		
	}
//	@ApiOperation(value = "Api to get mapped customer by their username")
//	@PostMapping("/changePassword")
//	public List<String> getAllMappedCusomer() {
//		log.info("Inside the getMappedCustomer controller");
//		return .getMappedCustomer(username);
//	}
	@ApiOperation(value = "Api to get the refresh token")
	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws StreamWriteException, DatabindException, IOException {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			HashMap<String, String> error = new HashMap<String, String>();
			try {
				String refresh_token = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier jwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(refresh_token);
				String username = decodedJWT.getSubject();
				AppUser user = service.getUser(username);
				String accesToken = JWT.create().withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURI().toString())
						.withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
						.sign(algorithm);
				Map<String, String> tokens = new HashMap<String, String>();
				tokens.put("accessToken", accesToken);
				response.setContentType(APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);

			} catch (IllegalArgumentException | JWTVerificationException e) {
				// TODO Auto-generated catch block
				log.error("Error logging in : {} ", e.getMessage());
				response.setHeader("error", e.getMessage());
				response.setStatus(HttpStatus.FORBIDDEN.value());
//				response.sendError(HttpStatus.FORBIDDEN.value());		
				error.put("error", e.getMessage());
				response.setContentType(APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);

			} catch (Exception e) {
				// TODO: handle excepion
				log.error("Error logging in : {} ", e.getMessage());
				response.setHeader("error", e.getMessage());
				response.setStatus(HttpStatus.FORBIDDEN.value());
//				response.sendError(HttpStatus.FORBIDDEN.value());		
				error.put("error", e.getMessage());
				response.setContentType(APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);

			}

		} else {
			throw new RuntimeException("Refresh token missing");
		}

	}
	@ApiOperation(value = "Api to check whether username is available of not")
	@PostMapping("/user/changePassword")
	public ResponseEntity<Object> changePassword(@RequestBody ChangePassword changePassword) {
		return new ResponseEntity(service.changePassword(changePassword.getOldPassword(),changePassword.getNewPassword()),HttpStatus.OK);
	}
}

@NoArgsConstructor
@AllArgsConstructor
@Data
class RoleToUserForm {
	private String username;
	private String name;

}

@NoArgsConstructor
@AllArgsConstructor
@Data
class ChangePassword {
	private String oldPassword;
	private String newPassword;

}

@NoArgsConstructor
@AllArgsConstructor
@Data
class Login {
	private String username;
	private String password;

}
@NoArgsConstructor
@AllArgsConstructor
@Data
class CheckUserName {
	private String username;

}


