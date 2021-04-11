package com.aitrades.blockchain.trade.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.client.DexSubGraphPriceFactoryClient;
import com.aitrades.blockchain.trade.domain.OrderDecision;
import com.aitrades.blockchain.trade.domain.orderType.OrderTypeRequest;
import com.aitrades.blockchain.trade.domain.orderType.OrderTypeResponse;

@Service
public class OrderDecisioner {
	
	private static final String BUY = "BUY";
	private static final String SELL = "SELL";
	
	@Autowired
	private DexSubGraphPriceFactoryClient dexSubGraphPriceFactoryClient;
	
	public OrderTypeResponse processLimitOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		try {
			BigDecimal currentPriceOfTicker = dexSubGraphPriceFactoryClient.getRoute(orderTypeRequest.getRoute()).tokenPrice(orderTypeRequest.getPairAddress(), orderTypeRequest.getRoute(), orderTypeRequest.getCredentials());
			boolean  triggerOrder= false;
			if(currentPriceOfTicker != null) {
				if(currentPriceOfTicker.compareTo(orderTypeRequest.getLimitPrice()) >= 0 && orderTypeRequest.getOrderSide().equalsIgnoreCase(SELL)) {
					triggerOrder = true;
				}else if(currentPriceOfTicker.compareTo(orderTypeRequest.getLimitPrice()) <= 0 && orderTypeRequest.getOrderSide().equalsIgnoreCase(BUY)) {
					triggerOrder = true;
				}
			}
			
			if(triggerOrder) {
				OrderTypeResponse orderTypeResponse = new OrderTypeResponse();
				orderTypeResponse.setDecision(OrderDecision.TRADE.name());
				orderTypeResponse.setExecutedPrice(currentPriceOfTicker);
				return orderTypeResponse;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public OrderTypeResponse processStopLossOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		try {
			BigDecimal currentPriceOfTicker = dexSubGraphPriceFactoryClient.getRoute(orderTypeRequest.getRoute()).tokenPrice(orderTypeRequest.getPairAddress(),  orderTypeRequest.getRoute(), orderTypeRequest.getCredentials());
			boolean  triggerOrder= false;
			if(currentPriceOfTicker != null) {
				if(currentPriceOfTicker.compareTo(orderTypeRequest.getStopPrice()) <= 0 && orderTypeRequest.getOrderSide().equalsIgnoreCase(SELL)) {
					triggerOrder = true;
				}else if(currentPriceOfTicker.compareTo(orderTypeRequest.getStopPrice()) >= 0 && orderTypeRequest.getOrderSide().equalsIgnoreCase(BUY)) {
					triggerOrder = true;// This is redudanted
				}
			}
			
			if(triggerOrder) {
				OrderTypeResponse orderTypeResponse = new OrderTypeResponse();
				orderTypeResponse.setDecision(OrderDecision.TRADE.name());
				orderTypeResponse.setExecutedPrice(currentPriceOfTicker);
				return orderTypeResponse;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public OrderTypeResponse processStopLimitOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		try {
			BigDecimal currentPriceOfTicker = dexSubGraphPriceFactoryClient.getRoute(orderTypeRequest.getRoute()).tokenPrice(orderTypeRequest.getPairAddress(), orderTypeRequest.getRoute(),  orderTypeRequest.getCredentials());
			
			boolean  triggerOrder= false;
			if(currentPriceOfTicker != null) {
				if(currentPriceOfTicker.compareTo(orderTypeRequest.getLimitPrice()) >= 0 && orderTypeRequest.getOrderSide().equalsIgnoreCase(SELL)) {
					triggerOrder = true;
				}else if(currentPriceOfTicker.compareTo(orderTypeRequest.getLimitPrice()) <= 0 && orderTypeRequest.getOrderSide().equalsIgnoreCase(BUY)) {
					triggerOrder = true;
				}
			}
			
			if(currentPriceOfTicker != null) {
				if(currentPriceOfTicker.compareTo(orderTypeRequest.getStopPrice()) <= 0 && orderTypeRequest.getOrderSide().equalsIgnoreCase(SELL)) {
					triggerOrder = true;
				}else if(currentPriceOfTicker.compareTo(orderTypeRequest.getStopPrice()) >= 0 && orderTypeRequest.getOrderSide().equalsIgnoreCase(BUY)) {
					triggerOrder = true;// This is redudanted
				}
			}
			
			if(triggerOrder) {
				OrderTypeResponse orderTypeResponse = new OrderTypeResponse();
				orderTypeResponse.setDecision(OrderDecision.TRADE.name());
				orderTypeResponse.setExecutedPrice(currentPriceOfTicker);
				return orderTypeResponse;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// in trail stop only we need to persist order
	public OrderTypeResponse processTrailingStopOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		try {

			BigDecimal currentPriceOfTicker = dexSubGraphPriceFactoryClient.getRoute(orderTypeRequest.getRoute())
																		   .tokenPrice(orderTypeRequest.getPairAddress(),  orderTypeRequest.getRoute(), orderTypeRequest.getCredentials());
			
			if (currentPriceOfTicker != null && (orderTypeRequest.getAdjustedPrice() == null || orderTypeRequest.getAdjustedPrice().compareTo(currentPriceOfTicker) <= 0)) {
				BigDecimal adjustedTrailingPrice = currentPriceOfTicker.subtract(currentPriceOfTicker.multiply(orderTypeRequest.getTrailPercent().divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)));
				OrderTypeResponse orderTypeResponse = new OrderTypeResponse();
				orderTypeResponse.setAdjustedPrice(adjustedTrailingPrice);
				return orderTypeResponse;
			} else if (currentPriceOfTicker != null && orderTypeRequest.getAdjustedPrice().compareTo(currentPriceOfTicker) > 0) {
				OrderTypeResponse orderTypeResponse = new OrderTypeResponse();
				orderTypeResponse.setDecision(OrderDecision.TRADE.name());
				return orderTypeResponse;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// in trail stop only we need to persist order
	public OrderTypeResponse processLimitTrailingStopOrder(OrderTypeRequest orderTypeRequest) throws Exception {
		try {
			  BigDecimal currentPriceOfTicker =	  dexSubGraphPriceFactoryClient.getRoute(orderTypeRequest.getRoute()).tokenPrice(orderTypeRequest.getPairAddress(),  orderTypeRequest.getRoute(), orderTypeRequest.getCredentials()); 
			  if (currentPriceOfTicker != null && (!orderTypeRequest.isLimitTrailingStopPriceMet() && orderTypeRequest.getAdjustedPrice().compareTo(currentPriceOfTicker) <=  0)) {
				  	OrderTypeResponse orderTypeResponse = new OrderTypeResponse();
					orderTypeResponse.setLimitTrailStopPriceMet(true);
					return orderTypeResponse;
			  }else {
				  return processTrailingStopOrder(orderTypeRequest);
			  }
				  
			 }catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}