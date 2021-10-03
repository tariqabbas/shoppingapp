package com.example.shoppingcart.dao;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import org.hibernate.query.Query;
import org.modelmapper.ModelMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.example.shoppingcart.entities.OrderDetailsEntity;
import com.example.shoppingcart.entities.OrdersEntity;
import com.example.shoppingcart.entities.UserEntity;
import com.example.shoppingcart.model.CartInfo;
import com.example.shoppingcart.model.OrderDetailInfo;
import com.example.shoppingcart.model.OrderInfo;
import com.example.shoppingcart.model.ReportInfo;
import com.example.shoppingcart.model.UserInfo;
import com.example.shoppingcart.pagination.PaginationResult;
import com.example.shoppingcart.util.ShoppingCartUtils;

import lombok.extern.log4j.Log4j2;

@Transactional
@Repository
@Log4j2
public class OrderDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private UserDAO userDao;

	@Autowired
	private OrderDetailsDao orderDetailsDao;

	@Autowired
	private ModelMapper mapper;

	private int getMaxOrderNum() {
		String sql = "Select max(o.id) from " + OrdersEntity.class.getName() + " o ";
		Session session = this.sessionFactory.getCurrentSession();
		session.createQuery(sql, Integer.class);
		Query<Integer> query = session.createQuery(sql, Integer.class);
		Integer value = query.getSingleResult();
		if (value == null) {
			return 0;
		}
		return value;
	}

	@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
	public void saveOrder(CartInfo orderInfo) {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			userDao.saveUser(mapper.map(orderInfo.getCustomerInfo(), UserInfo.class));

			int orderNum = this.getMaxOrderNum() + 1;
			OrdersEntity order = new OrdersEntity();

			Integer orderId = ShoppingCartUtils.getRandomNumberString();
			order.setId(orderId);
			order.setOrderNum(orderNum);
			order.setOrderDate(ShoppingCartUtils.getCurrentTimeStamp());
			order.setAmount(orderInfo.getOrderInfo().getAmount());
			// 0=not checkedOut yet ,1=checkedOut
			order.setStatus((byte) 0);
			order.setUserByUserId(userDao.findByUserName(orderInfo.getCustomerInfo().getUserName()));
			Integer savedorderId = (Integer) session.save(order);
			List<OrderDetailsEntity> lines = orderInfo.getDetailInfo().stream().map(m -> mapper.map(m, OrderDetailsEntity.class)).collect(Collectors.toList());
			for (OrderDetailsEntity line : lines) {
				OrderDetailsEntity detail = new OrderDetailsEntity();
				detail.setId(ShoppingCartUtils.getRandomNumberString().toString());
				detail.setOrdersByOrderId(mapper.map(findOrder(savedorderId), OrdersEntity.class));
				detail.setProductId(line.getProductId());
				detail.setUnitPrice(line.getUnitPrice());
				detail.setQuanity(line.getQuanity());
				detail.setAmount(line.getUnitPrice() * line.getQuanity());
				session.save(detail);
			}
			session.persist(order);
			// Flush
		} catch (Exception e) {
			log.error("exception in saving ===========", e.getMessage());
			e.printStackTrace();
		} finally {
			session.flush();
		}

	}

	@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
	public void updateOrder(CartInfo orderInfo) {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			OrdersEntity order = session.find(OrdersEntity.class, orderInfo.getOrderInfo().getId());
			order.setOrderNum(order.getOrderNum());
			order.setOrderDate(ShoppingCartUtils.getCurrentTimeStamp());
			order.setAmount(order.getAmount());
			// 0=not checkedOut yet ,1=checkedOut
			order.setStatus((byte) 0);
			order.setUserByUserId(order.getUserByUserId());
			session.update(order);
			List<OrderDetailsEntity> lines = orderInfo.getDetailInfo().stream().map(m -> mapper.map(m, OrderDetailsEntity.class)).collect(Collectors.toList());
			saveOrderDetails(session, order, lines);
			// Flush
		} catch (Exception e) {
			log.error("exception in updating ===========", e.getMessage());
			e.printStackTrace();
		} finally {
			session.flush();
		}
	}

	private void saveOrderDetails(Session session, OrdersEntity order, List<OrderDetailsEntity> lines) {
		for (OrderDetailsEntity line : lines) {
			if (line.getId().equals("0") || line.getId() == null) {
				OrderDetailsEntity detail = new OrderDetailsEntity();
				detail.setId(ShoppingCartUtils.getRandomNumberString().toString());
				detail.setOrdersByOrderId(order);
				detail.setProductId(line.getProductId());
				detail.setUnitPrice(line.getUnitPrice());
				detail.setQuanity(line.getQuanity());
				detail.setAmount(line.getUnitPrice() * line.getQuanity());
				session.save(detail);

			} else {
				OrderDetailsEntity detail = mapper.map(orderDetailsDao.getOrderDetailsById(Integer.parseInt(line.getId())), OrderDetailsEntity.class);
				detail.setId(detail.getId());
				detail.setOrdersByOrderId(order);
				detail.setProductId(line.getProductId());
				detail.setUnitPrice(line.getUnitPrice());
				detail.setQuanity(line.getQuanity());
				detail.setAmount(line.getUnitPrice() * line.getQuanity());
				session.merge(detail);
			}
		}
	}

	/* paginated Order Info */
	public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {
		String sql = "Select new " + OrderInfo.class.getName()//
				+ "(ord.id, ord.orderDate, ord.orderNum, ord.amount, " + " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone) " + " from " + OrdersEntity.class.getName() + " ord "//
				+ " order by ord.orderNum desc";

		Session session = this.sessionFactory.getCurrentSession();
		Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
		return new PaginationResult<>(query, page, maxResult, maxNavigationPage);
	}

	// find Order By Order ID
	public OrderInfo findOrder(int orderId) {
		Session session = this.sessionFactory.getCurrentSession();
		OrdersEntity entity = session.find(OrdersEntity.class, orderId);
		List<OrderDetailsEntity> orderDetailsList = entity.getOrderDetailsById().parallelStream().collect(Collectors.toList());
		OrderInfo info = new OrderInfo();
		info.setAmount(entity.getAmount());
		info.setId(entity.getId());
		info.setOrderDate(entity.getOrderDate());
		info.setOrderNum(entity.getOrderNum());
		info.setStatus(entity.getStatus());
		info.setUserId(entity.getUserByUserId().getId());
		info.setDetails(orderDetailsList.parallelStream().map(m -> {
			OrderDetailInfo orderDetails = new OrderDetailInfo();
			orderDetails.setAmount(m.getAmount());
			orderDetails.setId(m.getId());
			orderDetails.setOrderId(m.getOrdersByOrderId().getId());
			orderDetails.setUnitPrice(m.getUnitPrice());
			orderDetails.setProductId(m.getProductId());
			orderDetails.setQuanity(m.getQuanity());
			return orderDetails;
		}).collect(Collectors.toList()));
		info.setUserByUserId(mapper.map(entity.getUserByUserId(), UserInfo.class));
		return info;
	}

	/* exposing order info vid dto */
	public OrderInfo getOrderInfo(int orderId) {
		OrderInfo order = this.findOrder(orderId);
		if (order == null) {
			return null;
		}
		return new OrderInfo(order.getId(), order.getOrderDate(), order.getOrderNum(), order.getAmount(), order.getStatus(), order.getUserByUserId().getId());
	}

	/* listing order Details */
	public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
		String sql = "Select new " + OrderDetailInfo.class.getName() + "(d.id, d.product.code, d.product.name , d.quanity,d.price,d.amount) " + " from " + OrderDetailsEntity.class.getName() + " d " + " where d.order.id = :orderId ";

		Session session = this.sessionFactory.getCurrentSession();
		Query<OrderDetailInfo> query = session.createQuery(sql, OrderDetailInfo.class);
		query.setParameter("orderId", orderId);

		return query.getResultList();
	}

	/* delete order and order details */
	public boolean deleteOrder(int orderID) {
		Session session = this.sessionFactory.getCurrentSession();
		OrdersEntity ordersEntity = session.find(OrdersEntity.class, orderID);
		session.delete(ordersEntity);
//		String orderDetailsSql = "delete from " + OrderDetailsEntity.class.getName() + "e where e.orderId=:orderID";
//		Query<OrderDetailsEntity> query = session.createQuery(orderDetailsSql, OrderDetailsEntity.class);
//		query.setParameter("orderId", ordersEntity.getId());
//		int result = query.executeUpdate();
		return true;

	}

	/* generates report for most selling items */
	public List<ReportInfo> generateReportForMostSellingProduct() {
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "select new " + ReportInfo.class.getName() + "(o.productId ,sum(o.quanity)) " + " from " + OrderDetailsEntity.class.getName() + " o GROUP BY o.productId  order by sum(o.quanity) DESC";
		return session.createQuery(sql, ReportInfo.class).getResultList();

	}

}