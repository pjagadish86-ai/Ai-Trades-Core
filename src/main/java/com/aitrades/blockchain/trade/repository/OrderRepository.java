package com.aitrades.blockchain.trade.repository;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;

import com.aitrades.blockchain.trade.domain.Order;

@Repository
public class OrderRepository {

	@Resource(name = "orderMongoTemplate")
	public ReactiveMongoTemplate orderMongoTemplate;
	
	public void updateOrder(Order order) {
		orderMongoTemplate.save(order).block();
	}
}
