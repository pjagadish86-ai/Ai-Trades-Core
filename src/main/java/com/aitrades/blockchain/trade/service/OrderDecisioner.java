package com.aitrades.blockchain.trade.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.client.DexSubGraphPriceFactoryClient;
import com.aitrades.blockchain.trade.domain.OrderDecision;
import com.aitrades.blockchain.trade.domain.orderType.OrderTypeRequest;
import com.aitrades.blockchain.trade.domain.orderType.OrderTypeResponse;

@Service
//TODO: This class needs serious re-factoring of all dirty code.
public class OrderDecisioner {
	
	@Autowired
	private DexSubGraphPriceFactoryClient dexSubGraphPriceFactoryClient;
	
	public OrderTypeResponse processLimitOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		try {
			BigDecimal currentPriceOfTicker = dexSubGraphPriceFactoryClient.getRoute(orderTypeRequest.getRoute()).getPriceOfTicker(orderTypeRequest.getPairAddress());
			if(orderTypeRequest.getPrice().compareTo(currentPriceOfTicker) <= 0) {
				OrderTypeResponse orderTypeResponse = new OrderTypeResponse();
				orderTypeResponse.setDecision(OrderDecision.TRADE.name());
				return orderTypeResponse;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public OrderTypeResponse processStopLossOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		try {
			BigDecimal currentPriceOfTicker = dexSubGraphPriceFactoryClient.getRoute(orderTypeRequest.getRoute()).getPriceOfTicker(orderTypeRequest.getPairAddress());
			if(orderTypeRequest.getPrice().compareTo(currentPriceOfTicker) >= 0) {
				OrderTypeResponse orderTypeResponse = new OrderTypeResponse();
				orderTypeResponse.setDecision(OrderDecision.TRADE.name());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public OrderTypeResponse processStopLimitOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		try {
			//processStopLossOrder(tradeOrderMap);
			//processLimitOrder(tradeOrderMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// in trail stop only we need to persist order
	public OrderTypeResponse processTrailingStopOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		try {
			
			/*BigDecimal currentPriceOfTicker = dexSubGraphPriceFactoryClient.getRoute(orderTypeRequest.getRoute())
					.getPriceOfTicker(orderTypeRequest.getPairAddress());
			if (OrderSide.BUY.name().equalsIgnoreCase(order.getOrderEntity().getOrderSide())) {
				if (order.getOrderEntity().getTrailingStopOrder().getAdjustedtrailingStopPriceAsBigDecimal()
						.compareTo(currentPriceOfTicker) >= 0) { // 25 should be coming from order. BigDecimal
																	// adjustedTrailingPrice =
					currentPriceOfTicker.subtract(currentPriceOfTicker.multiply(
							new BigDecimal(25).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)));
					order.getOrderEntity().getTrailingStopOrder()
							.setAdjustedtrailingStopPriceAsBigDecimal(adjustedTrailingPrice);
				} else if (order.getOrderEntity().getTrailingStopOrder().getAdjustedtrailingStopPriceAsBigDecimal()
						.compareTo(currentPriceOfTicker) < 0) {
					tradeOrderMap.put(ORDER_DECISION, OrderDecision.BUY);
				}
			}
			if (OrderSide.SELL.name().equalsIgnoreCase(order.getOrderEntity().getOrderSide())) {
				if (order.getOrderEntity().getTrailingStopOrder().getAdjustedtrailingStopPriceAsBigDecimal()
						.compareTo(currentPriceOfTicker) <= 0) {
					BigDecimal adjustedTrailingPrice = currentPriceOfTicker.subtract(currentPriceOfTicker.multiply(
							new BigDecimal(25).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)));
					order.getOrderEntity().getTrailingStopOrder()
							.setAdjustedtrailingStopPriceAsBigDecimal(adjustedTrailingPrice);
				} else if (order.getOrderEntity().getTrailingStopOrder().getAdjustedtrailingStopPriceAsBigDecimal()
						.compareTo(currentPriceOfTicker) > 0) {
					tradeOrderMap.put(ORDER_DECISION, OrderDecision.SELL);
				}
			}*/
			 } catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// in trail stop only we need to persist order
	public OrderTypeResponse processLimitTrailingStopOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		try {
			/*
			 * BigDecimal currentPriceOfTicker =
			 * dexSubGraphPriceFactoryClient.getRoute(orderTypeRequest.getRoute())
			 * .getPriceOfTicker(orderTypeRequest.getPairAddress()); if
			 * (!order.getOrderEntity().getLimitTrailingStop().isLimitTrailingStopPriceMet()
			 * ) { if (order.getOrderEntity().getLimitTrailingStop().
			 * getAdjustedtrailingStopPriceAsBigDecimal().compareTo(currentPriceOfTicker) >=
			 * 0) {
			 * order.getOrderEntity().getLimitTrailingStop().setLimitTrailingStopPriceMet(
			 * true);// start trail now. } } else { processTrailingStopOrder(tradeOrderMap);
			 * }
			 * 
			 */} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}