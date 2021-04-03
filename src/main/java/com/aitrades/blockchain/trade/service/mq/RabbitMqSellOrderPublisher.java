package com.aitrades.blockchain.trade.service.mq;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.domain.Order;

@Service
public class RabbitMqSellOrderPublisher {

	@Value("${aitrades.order.submit.sell.rabbitmq.exchange}")
	String orderSubmitSellExchangeName;

	@Value("${aitrades.order.submit.sell.rabbitmq.routingkey}")
	private String orderSubmitSellRoutingkey;
	
	@Resource(name = "orderSubmitRabbitTemplate")
	public AmqpTemplate amqpTemplate;

	@Async
	public void send(Order order) {
		amqpTemplate.convertAndSend(orderSubmitSellExchangeName, orderSubmitSellRoutingkey, order);
		System.out.println("Send msg = " + order);
	}
}
