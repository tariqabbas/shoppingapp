package com.example.shoppingcart.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "products")
public class ProductsEntity {
	private int code;
	private byte[] image;
	private String name;
	private double productUnitPrice;
	private Timestamp createDate;
	private Integer availableStock;

	@Id
	@Column(name = "CODE")
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Basic
	@Column(name = "IMAGE")
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	@Basic
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Basic
	@Column(name = "PRODUCT_UNIT_PRICE")
	public double getProductUnitPrice() {
		return productUnitPrice;
	}

	public void setProductUnitPrice(double productUnitPrice) {
		this.productUnitPrice = productUnitPrice;
	}

	@Basic
	@Column(name = "CREATE_DATE")
	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	@Basic
	@Column(name = "available_stock")
	public Integer getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductsEntity that = (ProductsEntity) o;
		return code == that.code && Double.compare(that.productUnitPrice, productUnitPrice) == 0 && Arrays.equals(image, that.image) && Objects.equals(name, that.name) && Objects.equals(createDate, that.createDate) && Objects.equals(availableStock, that.availableStock);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(code, name, productUnitPrice, createDate, availableStock);
		result = 31 * result + Arrays.hashCode(image);
		return result;
	}
}
