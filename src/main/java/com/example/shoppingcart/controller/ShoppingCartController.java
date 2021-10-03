package com.example.shoppingcart.controller;

import java.util.List;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.shoppingcart.dao.OrderDAO;
import com.example.shoppingcart.dao.ProductDAO;
import com.example.shoppingcart.dto.ProductDto;
import com.example.shoppingcart.entities.OrdersEntity;
import com.example.shoppingcart.entities.ProductsEntity;
import com.example.shoppingcart.model.CartInfo;
import com.example.shoppingcart.model.OrderInfo;
import com.example.shoppingcart.model.ProductInfo;
import com.example.shoppingcart.model.ReportInfo;
import com.example.shoppingcart.pagination.PaginationResult;

import lombok.extern.log4j.Log4j2;

@RestController
@Transactional
@Log4j2
public class ShoppingCartController {

	@Autowired
	private ModelMapper mapper;
	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private ProductDAO productDAO;

	/* show Product List */
	@PostMapping("/productlist")
	public List<ProductInfo> listProducts(@RequestBody ProductDto dto) {
		log.info(" request received : " + dto);
		final int maxNavigationPage = 10;
		return productDAO.queryProducts(dto.getPage(), dto.getMaxResult(), maxNavigationPage, dto.getLikeName()).getList();

	}

	/* final checkout */
	@PostMapping("/checkout")
	public ResponseEntity<HttpStatus> checkOut(@RequestBody CartInfo cartInfo) {
		log.info(" request received : " + cartInfo);
		ResponseEntity<HttpStatus> res = null;
		try {
			// TODO check if stock is available before adding

			orderDAO.saveOrder(cartInfo);
			res = new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			res = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return res;
	}

	/* cancel order , deletes entries from db against user */
	@PostMapping("/cancelorder")
	public Boolean cancelOrder(@RequestParam(value = "orderId") Integer orderId) {
		boolean status = false;
		OrderInfo ordersEntity = orderDAO.findOrder(orderId);
		if (ordersEntity != null) {
			status = orderDAO.deleteOrder(ordersEntity.getId());
		}
		return status;

	}

	/* Show Order Information per user. */
	@GetMapping("/vieworder")
	public OrderInfo shoppingCartHandler(@RequestParam(name = "orderId") Integer orderId) {
		log.info("Order ID received : " + orderId);
		return orderDAO.findOrder(orderId);

//		return mapper.map(ordersEntity, OrderInfo.class);

	}

	/* Update Order information . */
	@PostMapping("/updateorder")
	public String shoppingCartCustomerForm(@RequestBody CartInfo cartInfo) {
		orderDAO.updateOrder(cartInfo);
		return "success";

	}

	@GetMapping("/getreport")
	public List<ReportInfo> getMostSellingItemsReport() {
		return orderDAO.generateReportForMostSellingProduct();

	}

	/* Show Product Information per by product code. */
	@GetMapping("/viewproduct")
	public ProductInfo viewProduct(@RequestParam(name = "code") Integer code) {
		log.info("Product Code Freceived : " + code);
		return productDAO.findProductInfo(code);

	}

}
