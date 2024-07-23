package com.tnqsoftware.customer_mapping_to_user.entity;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
