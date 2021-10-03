package com.example.shoppingcart.model;

import java.util.Objects;

import com.example.shoppingcart.entities.ProductsEntity;

public class ProductInfo {
	private Integer code;
	private String name;
	private Double price;
	private Integer availableStock;

	public ProductInfo() {
	}

	public ProductInfo(ProductsEntity product) {
		this.code = product.getCode();
		this.name = product.getName();
		this.price = product.getProductUnitPrice();
		this.availableStock = product.getAvailableStock();
	}

	// Using in JPA/Hibernate query

	public Integer getCode() {
		return code;
	}

	public ProductInfo(Integer code, String name, Double price, Integer availableStock) {
		super();
		this.code = code;
		this.name = name;
		this.price = price;
		this.availableStock = availableStock;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}

	@Override
	public String toString() {
		return "ProductInfo [code=" + code + ", name=" + name + ", price=" + price + ", availableStock=" + availableStock + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(availableStock, code, name, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductInfo other = (ProductInfo) obj;
		return Objects.equals(availableStock, other.availableStock) && Objects.equals(code, other.code) && Objects.equals(name, other.name) && Objects.equals(price, other.price);
	}

}
