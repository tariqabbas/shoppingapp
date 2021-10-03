package com.example.shoppingcart.dao;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import com.example.shoppingcart.entities.ProductsEntity;
import com.example.shoppingcart.model.ProductInfo;
import com.example.shoppingcart.pagination.PaginationResult;
import com.example.shoppingcart.util.ShoppingCartUtils;

import lombok.extern.log4j.Log4j2;

@Transactional
@Repository
@Log4j2
public class ProductDAO {

	@Autowired
	private SessionFactory sessionFactory;

	/*
	 * check if stock is availabe for a given product
	 */
	public Integer findProductAvailableStock(int code) {
		try {
			String sql = "Select e from " + ProductsEntity.class.getName() + " e Where e.code =:code ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<ProductsEntity> query = session.createQuery(sql, ProductsEntity.class);
			query.setParameter("code", code);
			return query.getSingleResult().getAvailableStock();
		} catch (NoResultException e) {
			log.error("Exception While checking stock Availability", e.getMessage());
			return null;
		}
	}

	public ProductsEntity findProduct(int code) {
		try {
			String sql = "Select e from " + ProductsEntity.class.getName() + " e Where e.code =:code ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<ProductsEntity> query = session.createQuery(sql, ProductsEntity.class);
			query.setParameter("code", code);
			return query.getSingleResult();
		} catch (NoResultException e) {
			log.error("Exception While finding product", e.getMessage());
			return null;
		}
	}

	public ProductInfo findProductInfo(int code) {
		ProductsEntity product = this.findProduct(code);
		if (product == null) {
			return null;
		}
		return new ProductInfo(product.getCode(), product.getName(), product.getProductUnitPrice(), product.getAvailableStock());
	}

	@org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(ProductInfo productForm) {

		Session session = this.sessionFactory.getCurrentSession();
		int code = productForm.getCode();

		ProductsEntity product = null;

		boolean isNew = false;
		if (code != 0) {
			product = this.findProduct(code);
		}
		if (product == null) {
			isNew = true;
			product = new ProductsEntity();
			product.setCreateDate(ShoppingCartUtils.getCurrentTimeStamp());
		}
		product.setCode(code);
		product.setName(productForm.getName());
		product.setProductUnitPrice(productForm.getPrice());
		if (isNew) {
			session.persist(product);
		}
		// If error in DB, Exceptions will be thrown out immediately
		session.flush();
	}

	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage, String likeName) {
		String sql = "Select new " + ProductInfo.class.getName() //
				+ "(p.code,p.name, p.productUnitPrice as price,p.availableStock ) " + " from "//
				+ ProductsEntity.class.getName() + " p ";
		if (likeName != null && likeName.length() > 0) {
			sql += " Where lower(p.name) like :likeName ";
		}
		sql += " order by p.createDate desc ";
		//
		Session session = this.sessionFactory.getCurrentSession();
		Query<ProductInfo> query = session.createQuery(sql, ProductInfo.class);

		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage) {
		return queryProducts(page, maxResult, maxNavigationPage, null);
	}

}