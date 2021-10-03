package com.example.shoppingcart.model;

import lombok.Data;

@Data
public class OrderDetailInfo {
	private String id;
	private int productId;
	private int orderId;
	private int quanity;
	private double unitPrice;
	private double amount;

}