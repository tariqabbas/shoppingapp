package com.example.shoppingcart.model;

import org.springframework.web.multipart.MultipartFile;
import com.example.shoppingcart.entities.ProductsEntity;

public class ProductForm {
	private int code;
	private String name;
	private double price;

	private boolean newProduct = false;

	// Upload file.
	private MultipartFile fileData;

	public ProductForm() {
		this.newProduct = true;
	}

	public ProductForm(ProductsEntity product) {
		this.code = product.getCode();
		this.name = product.getName();
		this.price = product.getProductUnitPrice();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public MultipartFile getFileData() {
		return fileData;
	}

	public void setFileData(MultipartFile fileData) {
		this.fileData = fileData;
	}

	public boolean isNewProduct() {
		return newProduct;
	}

	public void setNewProduct(boolean newProduct) {
		this.newProduct = newProduct;
	}

}
