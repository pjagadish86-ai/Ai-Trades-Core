package com.aitrades.blockchain.trade.computation;

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
			return orderComputation.limitOrder(prepareOrderTypeRequest(order));
		}
		
		else if(StringUtils.equalsIgnoreCase(OrderType.STOPLOSS.name(), order.getOrderEntity().getOrderType())) {
			 return orderComputation.stopLossOrder(prepareOrderTypeRequest(order));
		}
		
		else if(StringUtils.equalsIgnoreCase(OrderType.STOPLIMIT.name(), order.getOrderEntity().getOrderType())) {
			return orderComputation.stopLimitOrder(prepareOrderTypeRequest(order));
		}
		
		else if(StringUtils.equalsIgnoreCase(OrderType.TRAILLING_STOP.name(), order.getOrderEntity().getOrderType())) {
			 return orderComputation.trailingStopOrder(prepareOrderTypeRequest(order));
		}
		
		else if(StringUtils.equalsIgnoreCase(OrderType.LIMIT_TRAILLING_STOP.name(), order.getOrderEntity().getOrderType())) {
			 return orderComputation.limitTrailingStopOrder(prepareOrderTypeRequest(order));
		}
		
		else if(StringUtils.equalsIgnoreCase(OrderType.MARKET.name(), order.getOrderEntity().getOrderType())) {
			 return orderComputation.marketOrder(prepareOrderTypeRequest(order));
		}
		
		return null;
	}
	
	private OrderTypeRequest prepareOrderTypeRequest(Order order) {
		
		OrderTypeRequest orderTypeRequest = new OrderTypeRequest();
		orderTypeRequest.setRoute(order.getRoute());
		orderTypeRequest.setPairAddress(order.getPairData().getPairAddress().getAddress());
		orderTypeRequest.setCredentials(order.getCredentials());
		
		String orderType = order.getOrderEntity().getOrderType();
		
		if(StringUtils.equalsIgnoreCase(OrderType.LIMIT.name(), orderType)) {
			orderTypeRequest.setLimitPrice(order.getOrderEntity().getLimitOrder().getLimitPriceBigDecimal());
		}
		
		else if(StringUtils.equalsIgnoreCase(OrderType.STOPLOSS.name(), orderType)) {
			orderTypeRequest.setStopPrice(order.getOrderEntity().getStopOrder().getStopPriceBigDecimal());
		}
		
		else if(StringUtils.equalsIgnoreCase(OrderType.STOPLIMIT.name(), orderType)) {
			orderTypeRequest.setLimitPrice(order.getOrderEntity().getLimitOrder().getLimitPriceBigDecimal());
			orderTypeRequest.setStopPrice(order.getOrderEntity().getStopOrder().getStopPriceBigDecimal());
		}
		
		else if(StringUtils.equalsIgnoreCase(OrderType.TRAILLING_STOP.name(), orderType)) {
			orderTypeRequest.setTrailPercent(order.getOrderEntity().getTrailingStopOrder().getTrailingStopPercentBigDecimal());	
			orderTypeRequest.setAdjustedPrice(order.getOrderEntity().getTrailingStopOrder().getAdjustedtrailingStopPriceAsBigDecimal());
		}
		
		else if(StringUtils.equalsIgnoreCase(OrderType.LIMIT_TRAILLING_STOP.name(), orderType)) {
			orderTypeRequest.setLimitTrailingStopPriceMet(order.getOrderEntity().getLimitTrailingStop().isLimitTrailingStopPriceMet());
			orderTypeRequest.setLimitPrice(order.getOrderEntity().getLimitTrailingStop().getLimitPriceBigDecimal());
			orderTypeRequest.setTrailPercent(order.getOrderEntity().getLimitTrailingStop().getTrailingStopPercentBigDecimal());	
			orderTypeRequest.setAdjustedPrice(order.getOrderEntity().getLimitTrailingStop().getAdjustedtrailingStopPriceAsBigDecimal());
		}
		
		return orderTypeRequest;
	}

}
