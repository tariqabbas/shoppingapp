package com.example.shoppingcart.dao;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.shoppingcart.entities.OrderDetailsEntity;
import com.example.shoppingcart.model.OrderDetailInfo;
import lombok.extern.log4j.Log4j2;

@Transactional
@Repository
@Log4j2
public class OrderDetailsDao {
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private SessionFactory sessionFactory;

	public OrderDetailInfo getOrderDetailsById(Integer id) {
		String sql = "select new " + OrderDetailInfo.class.getName() + "() from " + OrderDetailsEntity.class.getName() + " od where od.id=:id";
		Session session = this.sessionFactory.getCurrentSession();
		OrderDetailInfo detailInfo = mapper.map(session.find(OrderDetailsEntity.class, id.toString()), OrderDetailInfo.class);
//		Query<OrderDetailInfo> query = session.createQuery(sql, OrderDetailInfo.class);
//		query.setParameter("id", id);
		log.info("order details response : " + detailInfo);
		return detailInfo;
	}
}
