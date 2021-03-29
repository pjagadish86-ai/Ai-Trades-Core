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

	@Resource(name = "orderMongoTemplate")
	public ReactiveMongoTemplate orderMongoTemplate;
	
	public void updateOrder(Order order) {
		orderMongoTemplate.save(order).block();
	}
	
	public void updateLock(Order order) {
		Query query = new Query();
        query.addCriteria(Criteria.where("id").is(order.getId()));
        Update update = new Update();
        update.set("read", "LOCK");
        orderMongoTemplate.updateFirst(query, update, Order.class).block();
	}
	
	public void updateAvail(Order order) {
		Query query = new Query();
        query.addCriteria(Criteria.where("id").is(order.getId()));
        Update update = new Update();
        update.set("read", "AVAL");
     //   update.set("counter", order.getCounter()+1); TODO: Come back
        orderMongoTemplate.updateFirst(query, update, Order.class).block();
	}
}
