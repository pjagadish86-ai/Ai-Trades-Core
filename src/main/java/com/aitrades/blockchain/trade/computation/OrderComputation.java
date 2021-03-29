package com.aitrades.blockchain.trade.computation;

import com.aitrades.blockchain.trade.domain.orderType.OrderTypeRequest;
import com.aitrades.blockchain.trade.domain.orderType.OrderTypeResponse;

public interface OrderComputation {

	public OrderTypeResponse marketOrder(OrderTypeRequest orderTypeRequest) throws Exception;

	public OrderTypeResponse limitOrder(OrderTypeRequest orderTypeRequest) throws Exception;
	
	public OrderTypeResponse stopLossOrder(OrderTypeRequest orderTypeRequest) throws Exception;
	
	public OrderTypeResponse stopLimitOrder(OrderTypeRequest orderTypeRequest) throws Exception;
	
	public OrderTypeResponse trailingStopOrder(OrderTypeRequest orderTypeRequest) throws Exception;
	
	public OrderTypeResponse limitTrailingStopOrder(OrderTypeRequest orderTypeRequest) throws Exception;
	
}
