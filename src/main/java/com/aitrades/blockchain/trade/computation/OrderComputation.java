package com.aitrades.blockchain.trade.computation;

import java.util.Map;

import com.aitrades.blockchain.trade.domain.Order;

public interface OrderComputation {

	public Map<String, Object> marketOrder(Order order, Map<String, Object> tradeOrderMap);

	public Map<String, Object> limitOrder(Order order, Map<String, Object> tradeOrderMap);
	
	public Map<String, Object> stopLossOrder(Order order, Map<String, Object> tradeOrderMap);
	
	public Map<String, Object> stopLimitOrder(Order order, Map<String, Object> tradeOrderMap);
	
	public Map<String, Object> trailingStopOrder(Order order, Map<String, Object> tradeOrderMap);
	
	public Map<String, Object> limitTrailingStopOrder(Order order, Map<String, Object> tradeOrderMap);
	
}
