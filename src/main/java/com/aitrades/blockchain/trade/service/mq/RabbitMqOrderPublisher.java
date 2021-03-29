package com.aitrades.blockchain.trade.service.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.domain.Order;
import com.aitrades.blockchain.trade.domain.OrderDecision;

@Service
public class RabbitMqOrderPublisher {

	@Autowired
	public RabbitMqBuyOrderPublisher buyOrderPublisher;
	
	@Autowired
	public RabbitMqSellOrderPublisher sellOrderPublisher;
	

	public void sendOrder(OrderDecision orderDecision, Order order) {
		if(orderDecision.equals(OrderDecision.BUY)) {
			buyOrderPublisher.send(order);
		}else if(orderDecision.equals(OrderDecision.SELL)) {
			sellOrderPublisher.send(order);
		}
	}

}
