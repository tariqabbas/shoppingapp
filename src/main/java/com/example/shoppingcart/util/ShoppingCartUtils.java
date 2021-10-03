package com.example.shoppingcart.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.example.shoppingcart.model.CartInfo;

public class ShoppingCartUtils {

	// Products in the cart, stored in Session.
	public static CartInfo getCartInSession(HttpServletRequest request) {

		CartInfo cartInfo = (CartInfo) request.getSession().getAttribute("myCart");

		if (cartInfo == null) {
			cartInfo = new CartInfo();

			request.getSession().setAttribute("myCart", cartInfo);
		}

		return cartInfo;
	}

	public static void removeCartInSession(HttpServletRequest request) {
		request.getSession().removeAttribute("myCart");
	}

	public static void storeLastOrderedCartInSession(HttpServletRequest request, CartInfo cartInfo) {
		request.getSession().setAttribute("lastOrderedCart", cartInfo);
	}

	public static CartInfo getLastOrderedCartInSession(HttpServletRequest request) {
		return (CartInfo) request.getSession().getAttribute("lastOrderedCart");
	}

	public static Timestamp getCurrentTimeStamp() {
		return new Timestamp(new Date().getTime());
	}

	public static Integer getRandomNumberString() {
		// It will generate 9 digit random Number.
		// from 0 to 999999999
		Random rnd = new Random();
		Integer number = rnd.nextInt(999999999);

		// this will convert any number sequence into 9 character.
		return number;
	}
}
