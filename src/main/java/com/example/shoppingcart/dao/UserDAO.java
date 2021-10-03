package com.example.shoppingcart.dao;

import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.shoppingcart.entities.UserEntity;
import com.example.shoppingcart.model.UserInfo;
import com.example.shoppingcart.util.ShoppingCartUtils;

@Transactional
@Repository
public class UserDAO {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private ModelMapper mapper;

	/* find user by userName */
	public UserEntity findByUserName(String userName) {
		String sql = "Select u from " + UserEntity.class.getName() + " u where u.userName=:userName";
		Session session = this.sessionFactory.getCurrentSession();
//		session.createQuery(sql, UserEntity.class);
		Query<UserEntity> query = session.createQuery(sql, UserEntity.class);
		query.setParameter("userName", userName);
		if (query.getResultList().size() != 0) {
			return query.getSingleResult();
		} else {
			return null;
		}

	}

	/* saving user info */
	public Integer saveUser(UserInfo userInfo) throws Exception {
		Session session = this.sessionFactory.getCurrentSession();
		Optional<UserEntity> user = Optional.ofNullable(findByUserName(userInfo.getUserName()));
		if (user.isPresent()) {
			throw new Exception("User already Exists " + userInfo.getUserName());
		} else {
			userInfo.setId(ShoppingCartUtils.getRandomNumberString());
			UserEntity userEntity = mapper.map(userInfo, UserEntity.class);
			return (Integer) session.save(userEntity);

		}
	}

	/* updates user info */
	public void updateUser(UserInfo userInfo) {
		Session session = this.sessionFactory.getCurrentSession();
		UserEntity user = findByUserName(userInfo.getUserName());
		if (user == null) {
			UserEntity userEntity = mapper.map(userInfo, UserEntity.class);
			session.update(userEntity);
		}
	}
}
