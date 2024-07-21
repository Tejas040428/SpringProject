package com.tnqsoftware.customer_mapping_to_user.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

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
public class Scope {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String scopeName;
	@ManyToMany
	@JoinTable(
	  name = "user_scope", 
	  joinColumns = @JoinColumn(name = "user_id",
			  						referencedColumnName = "id"), 
	  inverseJoinColumns = @JoinColumn(name = "scope_id",
			  						referencedColumnName = "id"))
	private Set<AppUser> appUsers;
	
	
}
