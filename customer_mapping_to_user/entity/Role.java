package com.tnqsoftware.customer_mapping_to_user.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
