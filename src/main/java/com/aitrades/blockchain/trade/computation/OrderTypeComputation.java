package com.aitrades.blockchain.trade.computation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.domain.Order;
import com.aitrades.blockchain.trade.domain.OrderDecision;
import com.aitrades.blockchain.trade.service.OrderDecisioner;

@Service
public class OrderTypeComputation implements OrderComputation{
	
	private static final String ORDER_DECISION = "ORDER_DECISION";
	
	@Autowired
	private OrderDecisioner orderDecisioner;

	//This is more of market buy.Do we need here?
	@Override
	public Map<String, Object> marketOrder(Order order, Map<String, Object> tradeOrderMap) {
		tradeOrderMap.put(ORDER_DECISION, OrderDecision.BUY);
		return tradeOrderMap;
	}

	@Override
	public Map<String, Object> limitOrder(Order order, Map<String, Object> tradeOrderMap) {
		return orderDecisioner.processLimitOrder(order, tradeOrderMap);
	}

	@Override
	public Map<String, Object> stopLossOrder(Order order, Map<String, Object> tradeOrderMap) {
		return orderDecisioner.processStopLossOrder(order, tradeOrderMap);
	}

	@Override
	public Map<String, Object> stopLimitOrder(Order order, Map<String, Object> tradeOrderMap) {
		return orderDecisioner.processStopLimitOrder(order, tradeOrderMap);
	}

	@Override
	public Map<String, Object> trailingStopOrder(Order order, Map<String, Object> tradeOrderMap) {
		return orderDecisioner.processTrailingStopOrder(order, tradeOrderMap);
	}

	@Override
	public Map<String, Object> limitTrailingStopOrder(Order order, Map<String, Object> tradeOrderMap) {
		return orderDecisioner.processLimitTrailingStopOrder(order, tradeOrderMap);
	}

}
