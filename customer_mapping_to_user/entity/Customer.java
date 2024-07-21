package com.tnqsoftware.customer_mapping_to_user.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Customer implements Serializable{

	@Id
	@Column(name="customer_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String customerName;
	private int isActive=0;
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY,
      cascade = {
          CascadeType.PERSIST,
          CascadeType.MERGE
      })
	@JoinTable(
	  name = "user_customer", 
	  joinColumns = @JoinColumn(name = "customer_id"), 
	  inverseJoinColumns = @JoinColumn(name = "user_id"))

	private Set<AppUser> appUsers;
	public Customer(Long id, String customerName, int isActive) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.isActive = isActive;
	}
	
	
}
