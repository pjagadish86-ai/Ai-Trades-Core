package com.aitrades.blockchain.trade.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.client.DexSubGraphPriceFactoryClient;
import com.aitrades.blockchain.trade.domain.Order;
import com.aitrades.blockchain.trade.domain.OrderDecision;
import com.aitrades.blockchain.trade.domain.side.OrderSide;

@Service
//TODO: This class needs serious re-factoring of all dirty code.
public class OrderDecisioner {
	
	@Autowired
	private DexSubGraphPriceFactoryClient dexSubGraphPriceFactoryClient;
	
	private static final String ORDER_DECISION = "ORDER_DECISION";

	@Async
	public Map<String, Object> processLimitOrder(Order order,  Map<String, Object> tradeOrderMap) {
		try {
			BigDecimal currentPriceOfTicker = dexSubGraphPriceFactoryClient.getRoute(order.getRoute()).getPriceOfTicker(order.getPairData().getPairAddress().getAddress());
			if(OrderSide.BUY.name().equalsIgnoreCase(order.getOrderEntity().getOrderSide())) {
				if(order.getOrderEntity().getLimitOrder().getLimitPriceBigDecimal().compareTo(currentPriceOfTicker) <= 0) {
					tradeOrderMap.put(ORDER_DECISION, OrderDecision.BUY);
				}
			}
			if(OrderSide.SELL.name().equalsIgnoreCase(order.getOrderEntity().getOrderSide())) {
				if(order.getOrderEntity().getLimitOrder().getLimitPriceBigDecimal().compareTo(currentPriceOfTicker) >= 0) {
					tradeOrderMap.put(ORDER_DECISION, OrderDecision.SELL);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tradeOrderMap;
	}
	
	@Async
	public Map<String, Object> processStopLossOrder(Order order,  Map<String, Object> tradeOrderMap) {
		try {
			BigDecimal currentPriceOfTicker = dexSubGraphPriceFactoryClient.getRoute(order.getRoute()).getPriceOfTicker(order.getPairData().getPairAddress().getAddress());
			if(OrderSide.BUY.name().equalsIgnoreCase(order.getOrderEntity().getOrderSide())) {
				if(order.getOrderEntity().getStopOrder().getStopPriceBigDecimal().compareTo(currentPriceOfTicker) >= 0) {
					tradeOrderMap.put(ORDER_DECISION, OrderDecision.BUY);
				}
			}
			
			if(OrderSide.SELL.name().equalsIgnoreCase(order.getOrderEntity().getOrderSide())) {
				if(order.getOrderEntity().getStopOrder().getStopPriceBigDecimal().compareTo(currentPriceOfTicker)  <= 0) {
					tradeOrderMap.put(ORDER_DECISION, OrderDecision.SELL);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tradeOrderMap;
	}
	
	public Map<String, Object> processStopLimitOrder(Order order,  Map<String, Object> tradeOrderMap) {
		try {
			processStopLossOrder(order, tradeOrderMap);
			processLimitOrder(order, tradeOrderMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tradeOrderMap;
	}
	// in trail stop only we need to persist order
	public Map<String, Object> processTrailingStopOrder(Order order,  Map<String, Object> tradeOrderMap) {
		try {
			BigDecimal currentPriceOfTicker = dexSubGraphPriceFactoryClient.getRoute(order.getRoute()).getPriceOfTicker(order.getPairData().getPairAddress().getAddress());
			if(OrderSide.BUY.name().equalsIgnoreCase(order.getOrderEntity().getOrderSide())) {
				if (order.getOrderEntity().getTrailingStopOrder().getAdjustedtrailingStopPriceAsBigDecimal().compareTo(currentPriceOfTicker) >= 0) {                    //25 should be coming from order.
					BigDecimal adjustedTrailingPrice = currentPriceOfTicker.subtract(currentPriceOfTicker.multiply(new BigDecimal(25).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)));
					order.getOrderEntity().getTrailingStopOrder().setAdjustedtrailingStopPriceAsBigDecimal(adjustedTrailingPrice);
				} else if (order.getOrderEntity().getTrailingStopOrder().getAdjustedtrailingStopPriceAsBigDecimal().compareTo(currentPriceOfTicker) < 0) {
					tradeOrderMap.put(ORDER_DECISION, OrderDecision.BUY);
				}
			}
			if(OrderSide.SELL.name().equalsIgnoreCase(order.getOrderEntity().getOrderSide())) {
				if (order.getOrderEntity().getTrailingStopOrder().getAdjustedtrailingStopPriceAsBigDecimal().compareTo(currentPriceOfTicker) <= 0) {
					BigDecimal adjustedTrailingPrice = currentPriceOfTicker.subtract(currentPriceOfTicker.multiply(new BigDecimal(25).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)));
					order.getOrderEntity().getTrailingStopOrder().setAdjustedtrailingStopPriceAsBigDecimal(adjustedTrailingPrice);
				} else if (order.getOrderEntity().getTrailingStopOrder().getAdjustedtrailingStopPriceAsBigDecimal().compareTo(currentPriceOfTicker) > 0) {
					tradeOrderMap.put(ORDER_DECISION, OrderDecision.SELL);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tradeOrderMap;
	}

	// in trail stop only we need to persist order
	public Map<String, Object> processLimitTrailingStopOrder(Order order,  Map<String, Object> tradeOrderMap) {
		try {
			BigDecimal currentPriceOfTicker = dexSubGraphPriceFactoryClient.getRoute(order.getRoute())
																           .getPriceOfTicker(order.getPairData().getPairAddress().getAddress());
			if (!order.getOrderEntity().getLimitTrailingStop().isLimitTrailingStopPriceMet()) {
				if (order.getOrderEntity().getLimitTrailingStop().getAdjustedtrailingStopPriceAsBigDecimal().compareTo(currentPriceOfTicker) >= 0) {
					order.getOrderEntity().getLimitTrailingStop().setLimitTrailingStopPriceMet(true);// start trail now.
				}
			} else {
				processTrailingStopOrder(order, tradeOrderMap);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tradeOrderMap;
	}

}