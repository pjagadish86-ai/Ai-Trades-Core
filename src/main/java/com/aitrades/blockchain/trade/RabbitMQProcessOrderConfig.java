package com.aitrades.blockchain.trade;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQProcessOrderConfig {

	@Value("${aitrades.order.process.rabbitmq.queue}")
	String orderProcessQueue;

	@Value("${aitrades.order.process.rabbitmq.exchange}")
	String orderProcessExchangeName;

	@Value("${aitrades.order.process.rabbitmq.routingkey}")
	private String orderProcessRoutingkey;
	
	@Bean("orderProcessQueue")
	public Queue orderProcessQueue() {
		return new Queue(orderProcessQueue, false);
	}

	@Bean("orderProcessDirectExchange")
	public DirectExchange orderProcessDirectExchange() {
		return new DirectExchange(orderProcessExchangeName);
	}

	@Bean("orderProcessBinding")
	public Binding orderProcessBinding() {
		return BindingBuilder.bind(orderProcessQueue())
							 .to(orderProcessDirectExchange())
							 .with(orderProcessRoutingkey);
	}
	
	@Bean
    public SimpleMessageListenerContainer orderSubmitBuyMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(orderProcessQueue());
      //  container.setMessageListener(new MessageListenerAdapter(new HelloWorldHandler()));
        container.setConcurrentConsumers(2);
        container.setDefaultRequeueRejected(false);
//	        container.setAdviceChain(new Advice[]{interceptor()});
        return container;
    }

}