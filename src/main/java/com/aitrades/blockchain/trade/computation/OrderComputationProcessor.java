package com.aitrades.blockchain.trade.computation;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.domain.Order;
import com.aitrades.blockchain.trade.domain.side.OrderType;

@Service
public class OrderComputationProcessor {

	@Autowired
	private OrderTypeComputation orderComputation;

	public Map<String, Object> processComputation(Order order, Map<String, Object> tradeOrderMap) throws Exception {
		if(StringUtils.equalsIgnoreCase(OrderType.LIMIT.name(), order.getOrderEntity().getOrderType())) {
			 return orderComputation.limitOrder(order, tradeOrderMap);
		}else if(StringUtils.equalsIgnoreCase(OrderType.STOPLOSS.name(), order.getOrderEntity().getOrderType())) {
			 return orderComputation.stopLossOrder(order, tradeOrderMap);
		}else if(StringUtils.equalsIgnoreCase(OrderType.STOPLIMIT.name(), order.getOrderEntity().getOrderType())) {
			 return orderComputation.stopLimitOrder(order, tradeOrderMap);
		}else if(StringUtils.equalsIgnoreCase(OrderType.TRAILLING_STOP.name(), order.getOrderEntity().getOrderType())) {
			 return orderComputation.trailingStopOrder(order, tradeOrderMap);
		}else if(StringUtils.equalsIgnoreCase(OrderType.LIMIT_TRAILLING_STOP.name(), order.getOrderEntity().getOrderType())) {
			 return orderComputation.limitTrailingStopOrder(order, tradeOrderMap);
		}else if(StringUtils.equalsIgnoreCase(OrderType.MARKET.name(), order.getOrderEntity().getOrderType())) {
			 return orderComputation.marketOrder(order, tradeOrderMap);
		}else {
			order.setOrderCode(89);
		}
		return tradeOrderMap;
	}
}
