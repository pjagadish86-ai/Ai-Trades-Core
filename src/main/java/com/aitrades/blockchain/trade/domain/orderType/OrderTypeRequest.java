package com.aitrades.blockchain.trade.domain.orderType;

import java.math.BigDecimal;

public class OrderTypeRequest {

	private String route;
	private String pairAddress;
	private BigDecimal price;
	private BigDecimal adjustedPrice;
	private BigDecimal trailPercent;
	
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
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
	
}
