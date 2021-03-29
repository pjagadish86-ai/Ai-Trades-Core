package com.aitrades.blockchain.trade.integration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import com.aitrades.blockchain.trade.RabbitMQProcessOrderConfig;

@Configuration
@ComponentScan("com.aitrades.blockchain.trade.integration")
@IntegrationComponentScan("com.aitrades.blockchain.trade.integration")
@EnableIntegration
public class OrderProcesssIntegrationConfig {

	@Autowired
	public RabbitMQProcessOrderConfig rabbitMQConfig;
	
	@Autowired
    private ConnectionFactory connectionFactory;
	
	@Bean
	@Autowired
	public IntegrationFlow processProduct() {
		return IntegrationFlows.from(Amqp.inboundAdapter(rabbitMQConfig.orderSubmitBuyMessageListenerContainer(connectionFactory)))
							   .handle("orderProcessGatewayEndpoint", "rabbitMqProcessOrderConsumer")
							   .handle("orderProcessGatewayEndpoint", "tradeOrdercomputationChannel")
							   .handle("orderProcessGatewayEndpoint", "transformInputChannel")
							   .handle("orderProcessGatewayEndpoint", "saveToMongoBasedOnOrderTypeChannel")
							   .handle("orderProcessGatewayEndpoint", "sendToOrderSubmitQueueChannel")
							   .channel(IntegrationContextUtils.NULL_CHANNEL_BEAN_NAME)
							   .get();
	}

	@Bean
	public OrderProcessGatewayEndpoint orderProcessGatewayEndpoint() {
		return new OrderProcessGatewayEndpoint();
	}

}
