package com.example.shoppingcart.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.shoppingcart.dao.ProductDAO;
import com.example.shoppingcart.entities.ProductsEntity;
import com.example.shoppingcart.model.ProductForm;

@Component
public class ProductFormValidator implements Validator {

	@Autowired
	private ProductDAO productDAO;

	// This validator only checks for the ProductForm.
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == ProductForm.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		ProductForm productForm = (ProductForm) target;

		// Check the fields of ProductForm.
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "NotEmpty.productForm.code");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.productForm.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty.productForm.price");

		int code = productForm.getCode();
		if (code != 0 && code > 0) {
			errors.rejectValue("code", "Pattern.productForm.code");
		} else if (productForm.isNewProduct()) {
			ProductsEntity product = productDAO.findProduct(code);
			if (product != null) {
				errors.rejectValue("code", "Duplicate.productForm.code");
			}
		}
	}

}
