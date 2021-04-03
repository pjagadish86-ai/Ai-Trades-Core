package com.aitrades.blockchain.trade.repository;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.aitrades.blockchain.trade.domain.Order;

@Repository
public class OrderRepository {

	private static final String ID = "id";
	private static final String LOCK = "LOCK";
	private static final String AVAL = "AVAL";
	private static final String READ = "read";
	
	@Resource(name = "orderMongoTemplate")
	public ReactiveMongoTemplate orderMongoTemplate;
	
	public void updateOrder(Order order) {
		orderMongoTemplate.save(order).block();
	}
	
	public void updateLock(Order order) {
		Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(order.getId()));
        Update update = new Update();
        update.set(READ, LOCK);
        orderMongoTemplate.updateFirst(query, update, Order.class).block();
	}
	
	public void updateAvail(Order order) {
		Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(order.getId()));
        Update update = new Update();
        update.set(READ, AVAL);
        orderMongoTemplate.updateFirst(query, update, Order.class).block();
	}
}
