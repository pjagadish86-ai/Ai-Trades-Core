package com.aitrades.blockchain.trade.domain.orderType;

import java.math.BigDecimal;

import org.web3j.crypto.Credentials;

public class OrderTypeRequest {

	private String route;
	private String pairAddress;
	private BigDecimal limitPrice;
	private BigDecimal adjustedPrice;
	private BigDecimal trailPercent;
	private BigDecimal stopPrice;
	
	private Credentials credentials;
	private boolean isLimitTrailingStopPriceMet;
	
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getPairAddress() {
		return pairAddress;
	}
	public void setPairAddress(String pairAddress) {
		this.pairAddress = pairAddress;
	}
	public BigDecimal getLimitPrice() {
		return limitPrice;
	}
	public void setLimitPrice(BigDecimal limitPrice) {
		this.limitPrice = limitPrice;
	}
	public BigDecimal getAdjustedPrice() {
		return adjustedPrice;
	}
	public void setAdjustedPrice(BigDecimal adjustedPrice) {
		this.adjustedPrice = adjustedPrice;
	}
	public BigDecimal getTrailPercent() {
		return trailPercent;
	}
	public void setTrailPercent(BigDecimal trailPercent) {
		this.trailPercent = trailPercent;
	}
	public BigDecimal getStopPrice() {
		return stopPrice;
	}
	public void setStopPrice(BigDecimal stopPrice) {
		this.stopPrice = stopPrice;
	}
	public boolean isLimitTrailingStopPriceMet() {
		return isLimitTrailingStopPriceMet;
	}
	public void setLimitTrailingStopPriceMet(boolean isLimitTrailingStopPriceMet) {
		this.isLimitTrailingStopPriceMet = isLimitTrailingStopPriceMet;
	}
	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
	
}
