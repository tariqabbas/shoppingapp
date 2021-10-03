package com.example.shoppingcart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfo {

	private int id;
	private String address;
	private String email;
	private String phone;
	private String userName;
	private String firstName;
	private String lastName;
	private String encrytedPassword;
	private String userRole;
	private boolean active;
	private boolean valid;

}