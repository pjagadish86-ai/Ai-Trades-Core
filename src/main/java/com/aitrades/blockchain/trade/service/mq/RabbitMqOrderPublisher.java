package com.aitrades.blockchain.trade.service.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.domain.Order;
import com.aitrades.blockchain.trade.domain.OrderDecision;

@Service
public class RabbitMqOrderPublisher {

	@Autowired
	private RabbitMqBuyOrderPublisher buyOrderPublisher;
	
	@Autowired
	private RabbitMqSellOrderPublisher sellOrderPublisher;
	
	@Async
	public void sendOrder(String orderDecision, Order order) {
		if(orderDecision.equalsIgnoreCase(OrderDecision.BUY.name())) {
			buyOrderPublisher.send(order);
		}else if(orderDecision.equalsIgnoreCase(OrderDecision.SELL.name())) {
			sellOrderPublisher.send(order);
		}
	}

}
