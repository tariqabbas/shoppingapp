package com.example.shoppingcart.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class OrdersEntity {
	private int id;
	private int orderNum;
	private Timestamp orderDate;
	private double amount;
//    private int userId;
	private byte status;
	private Collection<OrderDetailsEntity> orderDetailsById;
	private UserEntity userByUserId;

	@Id
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Basic
	@Column(name = "ORDER_NUM")
	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	@Basic
	@Column(name = "ORDER_DATE")
	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	@Basic
	@Column(name = "AMOUNT")
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

//    @Basic
//    @Column(name = "USER_ID")
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }

	@Basic
	@Column(name = "STATUS")
	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OrdersEntity that = (OrdersEntity) o;
		return id == that.id && orderNum == that.orderNum && Double.compare(that.amount, amount) == 0 &&
//                userId == that.userId &&
				status == that.status && Objects.equals(orderDate, that.orderDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderNum, orderDate, amount, status);
	}

//	@JsonIgnore
	@OneToMany(mappedBy = "ordersByOrderId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Collection<OrderDetailsEntity> getOrderDetailsById() {
		return orderDetailsById;
	}

	public void setOrderDetailsById(Collection<OrderDetailsEntity> orderDetailsById) {
		this.orderDetailsById = orderDetailsById;
	}

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = true)
	public UserEntity getUserByUserId() {
		return userByUserId;
	}

	public void setUserByUserId(UserEntity userByUserId) {
		this.userByUserId = userByUserId;
	}
}
