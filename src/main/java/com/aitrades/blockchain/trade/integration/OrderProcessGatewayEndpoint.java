package com.aitrades.blockchain.trade.integration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;

import com.aitrades.blockchain.trade.computation.OrderComputationProcessor;
import com.aitrades.blockchain.trade.domain.Order;
import com.aitrades.blockchain.trade.domain.OrderType;
import com.aitrades.blockchain.trade.domain.orderType.OrderTypeResponse;
import com.aitrades.blockchain.trade.domain.side.OrderState;
import com.aitrades.blockchain.trade.repository.OrderRepository;
import com.aitrades.blockchain.trade.service.mq.RabbitMqOrderPublisher;
import com.fasterxml.jackson.databind.ObjectReader;
public class OrderProcessGatewayEndpoint {
	
	private static final String ORDER = "ORDER";
	private static final String ORDER_DECISION = "ORDER_DECISION";

	@Autowired
	private OrderComputationProcessor orderComputationProcessor;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Resource(name= "orderObjectReader")
	private ObjectReader orderObjectReader;
	
	@Autowired
	private RabbitMqOrderPublisher rabbitMqOrderPublisher;
	
	@Transformer(inputChannel = "rabbitMqProcessOrderConsumer", outputChannel = "tradeOrdercomputationChannel")
	public Map<String, Object> rabbitMqProcessOrderConsumer(byte[] message) throws Exception{
		Order order  = orderObjectReader.readValue(message);
		Map<String, Object> aitradesMap = new ConcurrentHashMap<>();
		aitradesMap.put(ORDER, order);
		return aitradesMap;
	}

	@ServiceActivator(inputChannel = "tradeOrdercomputationChannel", outputChannel = "transformInputChannel")
	public Map<String, Object> tradeOrdercomputationChannel(Map<String, Object> tradeOrderMap) throws Exception{
		Order order = (Order)tradeOrderMap.get(ORDER);
		if(order.getOrderCode() != null && orderStateCheck(order)) {
			OrderTypeResponse orderTypeResponse = orderComputationProcessor.processComputation(order);
			 if(orderTypeResponse != null) {
				 if(orderTypeResponse.getAdjustedPrice() != null) {
					 if(order.getOrderEntity().getOrderType().equalsIgnoreCase(OrderType.TRAILLING_STOP.name())) {
						 order.getOrderEntity().getTrailingStopOrder().setAdjustedtrailingStopPriceAsBigDecimal(orderTypeResponse.getAdjustedPrice());
					 }
					 if(order.getOrderEntity().getOrderType().equalsIgnoreCase(OrderType.LIMIT_TRAILLING_STOP.name())) {
						 order.getOrderEntity().getLimitTrailingStop().setLimitTrailingStopPriceMet(orderTypeResponse.isLimitTrailStopPriceMet());
						 order.getOrderEntity().getLimitTrailingStop().setAdjustedtrailingStopPriceAsBigDecimal(orderTypeResponse.getAdjustedPrice());
					 }
				 }
				 if(orderTypeResponse.getDecision() != null) {
					 tradeOrderMap.put(ORDER_DECISION, order.getOrderEntity().getOrderSide());
				 }
			 }
		}
		return tradeOrderMap;
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
			rabbitMqOrderPublisher.sendOrder((String)tradeOrderMap.get(ORDER_DECISION), order);
		}
		return tradeOrderMap;
	}
	
	private boolean orderStateCheck(Order order) {
		return OrderState.WORKING.name().equalsIgnoreCase(order.getOrderEntity().getOrderState()) 
				|| OrderState.PARTIAL_FILLED.name().equalsIgnoreCase(order.getOrderEntity().getOrderState()) ;
	}
}
