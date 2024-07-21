package com.tnqsoftware.customer_mapping_to_user.requestEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class RequestCustomerMapping {

	@Id
	private String username;
	@ElementCollection
	@CollectionTable(name = "listCustomerName", joinColumns = @JoinColumn(name = "username"))
	private List<String> custName = new ArrayList<>();
}