package com.aitrades.blockchain.trade.computation;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.domain.Order;
import com.aitrades.blockchain.trade.domain.orderType.OrderTypeRequest;
import com.aitrades.blockchain.trade.domain.orderType.OrderTypeResponse;
import com.aitrades.blockchain.trade.domain.side.OrderType;

@Service
public class OrderComputationProcessor {

	@Autowired
	private OrderTypeComputation orderComputation;

	public OrderTypeResponse processComputation(Order order) throws Exception {
		if(StringUtils.equalsIgnoreCase(OrderType.LIMIT.name(), order.getOrderEntity().getOrderType())) {
			return orderComputation.limitOrder(prepareOrderTypeRequest(order.getRoute(), order.getPairData().getPairAddress().getAddress(), order.getOrderEntity().getLimitOrder().getLimitPriceBigDecimal()));
		}else if(StringUtils.equalsIgnoreCase(OrderType.STOPLOSS.name(), order.getOrderEntity().getOrderType())) {
			 return orderComputation.stopLossOrder(prepareOrderTypeRequest(order.getRoute(), order.getPairData().getPairAddress().getAddress(), order.getOrderEntity().getLimitOrder().getLimitPriceBigDecimal()));
		}else if(StringUtils.equalsIgnoreCase(OrderType.STOPLIMIT.name(), order.getOrderEntity().getOrderType())) {
			OrderTypeRequest orderTypeRequest = new OrderTypeRequest();
			return orderComputation.stopLimitOrder(orderTypeRequest);
		}else if(StringUtils.equalsIgnoreCase(OrderType.TRAILLING_STOP.name(), order.getOrderEntity().getOrderType())) {
			OrderTypeRequest orderTypeRequest = new OrderTypeRequest();
			 return orderComputation.trailingStopOrder(orderTypeRequest);
		}else if(StringUtils.equalsIgnoreCase(OrderType.LIMIT_TRAILLING_STOP.name(), order.getOrderEntity().getOrderType())) {
			OrderTypeRequest orderTypeRequest = new OrderTypeRequest();
			 return orderComputation.limitTrailingStopOrder(orderTypeRequest);
		}else if(StringUtils.equalsIgnoreCase(OrderType.MARKET.name(), order.getOrderEntity().getOrderType())) {
			OrderTypeRequest orderTypeRequest = new OrderTypeRequest();
			 return orderComputation.marketOrder(orderTypeRequest);
		}
		return null;
	}
	
	private OrderTypeRequest prepareOrderTypeRequest(String route, String pairAddress, BigDecimal price) {
		OrderTypeRequest orderTypeRequest = new OrderTypeRequest();
		orderTypeRequest.setRoute(route);
		orderTypeRequest.setPairAddress(pairAddress);
		orderTypeRequest.setPrice(price);
		return orderTypeRequest;
	}

}
