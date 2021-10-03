package com.example.shoppingcart.model;

import lombok.Data;

@Data
public class UserInfo {
	private int id;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private String encrytedPassword;
	private String userRole;
	private boolean active;
	private String address;
	private Integer phone;
}
