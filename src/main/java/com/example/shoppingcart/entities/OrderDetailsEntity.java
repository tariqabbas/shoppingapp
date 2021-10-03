package com.example.shoppingcart.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_details")
public class OrderDetailsEntity {
	private String id;
	private int productId;
//	private int orderId;
	private double unitPrice;
	private int quanity;
	private double amount;
	private OrdersEntity ordersByOrderId;

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Basic
	@Column(name = "PRODUCT_ID")
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

//	@Basic
//	@Column(name = "ORDER_ID")
//	public int getOrderId() {
//		return orderId;
//	}
//
//	public void setOrderId(int orderId) {
//		this.orderId = orderId;
//	}

	@Basic
	@Column(name = "UNIT_PRICE")
	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Basic
	@Column(name = "QUANITY")
	public int getQuanity() {
		return quanity;
	}

	public void setQuanity(int quanity) {
		this.quanity = quanity;
	}

	@Basic
	@Column(name = "AMOUNT")
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OrderDetailsEntity that = (OrderDetailsEntity) o;
		return productId == that.productId && Double.compare(that.unitPrice, unitPrice) == 0 && quanity == that.quanity && Double.compare(that.amount, amount) == 0 && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, productId,  unitPrice, quanity, amount);
	}

	@ManyToOne
	@JoinColumn(name = "ORDER_ID", referencedColumnName = "ID", nullable = true)
	public OrdersEntity getOrdersByOrderId() {
		return ordersByOrderId;
	}

	public void setOrdersByOrderId(OrdersEntity ordersByOrderId) {
		this.ordersByOrderId = ordersByOrderId;
	}
}
