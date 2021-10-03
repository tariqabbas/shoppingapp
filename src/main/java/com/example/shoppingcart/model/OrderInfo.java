package com.example.shoppingcart.model;

import java.util.Date;
import java.util.List;

import com.example.shoppingcart.entities.OrderDetailsEntity;
import com.example.shoppingcart.entities.UserEntity;

public class OrderInfo {

	private int id;
	private Date orderDate;
	private int orderNum;
	private double amount;
	private byte status;
	private int userId;
	private UserInfo userByUserId;
	private List<OrderDetailInfo> orderDetails;

	public OrderInfo() {

	}

	// Using for Hibernate Query.
	public OrderInfo(int id, Date orderDate, int orderNum, double amount, byte status, int userId) {
		this.id = id;
		this.orderDate = orderDate;
		this.orderNum = orderNum;
		this.amount = amount;
		this.status = status;
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public UserInfo getUserByUserId() {
		return userByUserId;
	}

	public void setUserByUserId(UserInfo userByUserId) {
		this.userByUserId = userByUserId;
	}

	public List<OrderDetailInfo> getDetails() {
		return orderDetails;
	}

	public void setDetails(List<OrderDetailInfo> details) {
		this.orderDetails = details;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}