package com.example.shoppingcart.model;

import java.util.List;

import lombok.Data;

@Data
public class CartInfo {
	private int orderNum;
	private OrderInfo orderInfo;
	private CustomerInfo customerInfo;
	private List<OrderDetailInfo> detailInfo;

}