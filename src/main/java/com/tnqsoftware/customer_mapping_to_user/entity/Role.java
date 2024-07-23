package com.tnqsoftware.customer_mapping_to_user.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
public class Role {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	
	  @ManyToMany(fetch = FetchType.LAZY,
		      cascade = {
		          CascadeType.PERSIST,
		          CascadeType.MERGE
		      })
		  @JoinTable(name = "user_roles",
		        joinColumns = { @JoinColumn(name = "role_id") },
		        inverseJoinColumns = { @JoinColumn(name = "user_id") })
	private Set<AppUser> appUsers;
	
}
