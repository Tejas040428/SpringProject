package com.tnqsoftware.customer_mapping_to_user.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter

@Getter

@AllArgsConstructor

@NoArgsConstructor

@ToString

@Entity
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotEmpty
	private String name;
	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z0-9]{5,10}")
	@Size(min = 5, max = 10)
	private String username;
	@NotEmpty
	private String password;
	private Date createdDate;
	private Date updatedDate;
	@JsonIgnore
	@ManyToMany( cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, mappedBy = "appUsers")
	private Set<Role> roles = new HashSet<Role>();
	@JsonIgnore
	@ManyToMany( cascade = { CascadeType.ALL }, mappedBy = "appUsers")
	
	private Set<Customer> customers = new HashSet<Customer>();

	public AppUser(Long id, String name, String username, String password, Date createdDate, Date updatedDate) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

}
