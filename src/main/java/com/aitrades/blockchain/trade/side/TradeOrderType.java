package com.aitrades.blockchain.trade.side;

public enum TradeOrderType {
	
	MARKET("MARKET", 1), 
	LIMIT("LIMIT", 2), 
	STOPLOSS("STOPLOSS", 3),
	STOPLIMIT("STOPLIMIT", 4),
	TRAILLINGSTOP("TRAILLINGSTOP", 5),
	LIMIT_TRAILLINGSTOP("LIMIT_TRAILLINGSTOP", 6);

	private final String value;
	private final Integer sortorder;
	
	private TradeOrderType(String value, Integer sortorder) {
		this.value = value;
		this.sortorder = sortorder;
	}
	
	public String getValue() {
		return value;
	}

	public Integer getSortorder() {
		return sortorder;
	}

	public static TradeOrderType fromValue(String value) {
		for (final TradeOrderType orderType : TradeOrderType.values()) {
			if (orderType.value.equalsIgnoreCase(value)) {
				return orderType;
			}
		}
		throw new IllegalArgumentException(value);
	}

	public static TradeOrderType fromName(String name) {

		for (final TradeOrderType orderType : TradeOrderType.values()) {
			if (orderType.name().equalsIgnoreCase(name)) {
				return orderType;
			}
		}
		throw new IllegalArgumentException(name);

	}
	
}
