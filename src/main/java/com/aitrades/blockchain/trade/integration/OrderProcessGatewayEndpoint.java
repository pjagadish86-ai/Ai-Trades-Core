package com.aitrades.blockchain.trade.integration;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;

import com.aitrades.blockchain.trade.computation.OrderComputationProcessor;
import com.aitrades.blockchain.trade.domain.Order;
import com.aitrades.blockchain.trade.domain.OrderDecision;
import com.aitrades.blockchain.trade.domain.side.OrderState;
import com.aitrades.blockchain.trade.repository.OrderRepository;
import com.aitrades.blockchain.trade.service.mq.RabbitMqOrderPublisher;
import com.fasterxml.jackson.databind.ObjectReader;
import com.jsoniter.JsonIterator;
public class OrderProcessGatewayEndpoint {
	
	private static final String ORDER = "ORDER";
	private static final String ORDER_DECISION = "ORDER_DECISION";

	@Autowired
	private OrderComputationProcessor orderComputationProcessor;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Resource(name= "orderObjectReader")
	public ObjectReader orderObjectReader;
	
	@Autowired
	private RabbitMqOrderPublisher rabbitMqOrderPublisher;
	
	@Transformer(inputChannel = "rabbitMqProcessOrderConsumer", outputChannel = "tradeOrdercomputationChannel")
	public Map<String, Object> rabbitMqProcessOrderConsumer(byte[] message) throws Exception{
		Order order  = orderObjectReader.readValue(message);
		Map<String, Object> aitradesMap = new ConcurrentHashMap<String, Object>();
		aitradesMap.put(ORDER, order);
		return aitradesMap;
	}

	@ServiceActivator(inputChannel = "tradeOrdercomputationChannel", outputChannel = "transformInputChannel")
	public Map<String, Object> tradeOrdercomputationChannel(Map<String, Object> tradeOrderMap) throws Exception{
		Order order = (Order)tradeOrderMap.get(ORDER);
		return order.getOrderCode() != null && orderStateCheck(order) ? orderComputationProcessor.processComputation(order, tradeOrderMap) : tradeOrderMap;
	}

	@ServiceActivator(inputChannel = "transformInputChannel", outputChannel = "saveToMongoBasedOnOrderTypeChannel")
	public Map<String, Object>  transformInputChannel(Map<String, Object> tradeOrderMap){
		Order order = (Order)tradeOrderMap.get(ORDER);
		if(order.getOrderCode() != null && tradeOrderMap.get(ORDER_DECISION) != null) {
			order.setOrderCode(84);
		}else {
			 order.setOrderCode(83);
		}
		return tradeOrderMap;
	}
	
	@ServiceActivator(inputChannel = "saveToMongoBasedOnOrderTypeChannel", outputChannel = "sendToOrderSubmitQueueChannel")
	public Map<String, Object> saveToMongoBasedOnOrderTypeChannel(Map<String, Object> tradeOrderMap){
		Order order = (Order)tradeOrderMap.get(ORDER);
		if(order.getOrderCode() != null && (order.getOrderCode().equals(83) || order.getOrderCode().equals(84))) {
			orderRepository.updateOrder(order);
		}
		return tradeOrderMap;
	}
	
	@ServiceActivator(inputChannel = "sendToOrderSubmitQueueChannel")
	public Map<String, Object> sendToOrderSubmitQueueChannel(Map<String, Object> tradeOrderMap) throws Exception{
		Order order = (Order)tradeOrderMap.get(ORDER);
		if(order.getOrderCode() != null && order.getOrderCode().equals(84)) {
			rabbitMqOrderPublisher.sendOrder((OrderDecision)tradeOrderMap.get(ORDER_DECISION), order);
		}
		return tradeOrderMap;
	}
	
	private boolean orderStateCheck(Order order) {
		return OrderState.WORKING.name().equalsIgnoreCase(order.getOrderEntity().getOrderState()) 
				|| OrderState.PARTIAL_FILLED.name().equalsIgnoreCase(order.getOrderEntity().getOrderState()) ;
	}
}
