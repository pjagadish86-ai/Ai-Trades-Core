package com.aitrades.blockchain.trade.computation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.domain.orderType.OrderTypeRequest;
import com.aitrades.blockchain.trade.domain.orderType.OrderTypeResponse;
import com.aitrades.blockchain.trade.service.OrderDecisioner;

@Service
public class OrderTypeComputation implements OrderComputation{
	
	private static final String TRADE = "TRADE";
	
	@Autowired
	private OrderDecisioner orderDecisioner;

	@Override
	public OrderTypeResponse marketOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		OrderTypeResponse orderTypeResponse = new OrderTypeResponse();
		orderTypeResponse.setDecision(TRADE);
		return orderTypeResponse;
	}

	@Override
	public OrderTypeResponse limitOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		return orderDecisioner.processLimitOrder(orderTypeRequest);
	}

	@Override
	public OrderTypeResponse stopLossOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		return orderDecisioner.processStopLossOrder(orderTypeRequest);
	}

	@Override
	public OrderTypeResponse stopLimitOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		return orderDecisioner.processStopLimitOrder(orderTypeRequest);
	}

	@Override
	public OrderTypeResponse trailingStopOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		return orderDecisioner.processTrailingStopOrder(orderTypeRequest);
	}

	@Override
	public OrderTypeResponse limitTrailingStopOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		return orderDecisioner.processLimitTrailingStopOrder(orderTypeRequest);
	}

}
