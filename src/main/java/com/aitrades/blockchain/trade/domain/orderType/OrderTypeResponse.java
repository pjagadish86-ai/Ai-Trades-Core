package com.aitrades.blockchain.trade.domain.orderType;

import java.math.BigDecimal;

public class OrderTypeResponse {

	private String decision;
	private BigDecimal adjustedPrice;
	private boolean isLimitTrailStopPriceMet;
	private BigDecimal executedPrice;
	
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	public BigDecimal getAdjustedPrice() {
		return adjustedPrice;
	}
	public void setAdjustedPrice(BigDecimal adjustedPrice) {
		this.adjustedPrice = adjustedPrice;
	}
	public boolean isLimitTrailStopPriceMet() {
		return isLimitTrailStopPriceMet;
	}
	public void setLimitTrailStopPriceMet(boolean isLimitTrailStopPriceMet) {
		this.isLimitTrailStopPriceMet = isLimitTrailStopPriceMet;
	}
	public BigDecimal getExecutedPrice() {
		return executedPrice;
	}
	public void setExecutedPrice(BigDecimal executedPrice) {
		this.executedPrice = executedPrice;
	}
	
}
