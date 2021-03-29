package com.aitrades.blockchain.trade.service.mq;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.domain.Order;

@Service
public class RabbitMqSnipeOrderPublisher {

	@Value("${aitrades.order.submit.snipe.rabbitmq.exchange}")
	String orderSubmitSnipeExchangeName;

	@Value("${aitrades.order.submit.snipe.rabbitmq.routingkey}")
	private String orderSubmitSnipeRoutingkey;

	@Resource(name = "orderSubmitRabbitTemplate")
	public AmqpTemplate amqpTemplate;

	public void send(Order transactionRequest) {
		amqpTemplate.convertAndSend(orderSubmitSnipeExchangeName, orderSubmitSnipeRoutingkey, transactionRequest);
		System.out.println("Send msg = " + transactionRequest);
	}
}
