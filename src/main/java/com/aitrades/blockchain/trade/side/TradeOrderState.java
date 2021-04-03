package com.aitrades.blockchain.trade.side;

public enum TradeOrderState {

	FILLED("FILLED", 1), 
	WORKING("WORKING", 2), 
	CANCELLED("CANCELLED", 3);
	
	private final String value;
	private final Integer sortorder;
	private TradeOrderState(String value, Integer sortorder) {
		this.value = value;
		this.sortorder = sortorder;
	}
	
	public String getValue() {
		return value;
	}
	public Integer getSortorder() {
		return sortorder;
	}
	
	public static TradeOrderState fromValue(String value) {
		for (final TradeOrderState orderStatus : TradeOrderState.values()) {
			if (orderStatus.value.equalsIgnoreCase(value)) {
				return orderStatus;
			}
		}
		throw new IllegalArgumentException(value);
	}

	public static TradeOrderState fromName(String name) {

		for (final TradeOrderState orderType : TradeOrderState.values()) {
			if (orderType.name().equalsIgnoreCase(name)) {
				return orderType;
			}
		}
		throw new IllegalArgumentException(name);

	}
	
}
