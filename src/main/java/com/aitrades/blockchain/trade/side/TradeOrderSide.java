package com.aitrades.blockchain.trade.side;


public enum TradeOrderSide {
	BUY("BUY", 1), SELL("SELL", 2), CANCEL("CANCEL", 3);

	private final String value;
	private final Integer sortorder;

	private TradeOrderSide(String value, Integer sortorder) {
		this.value = value;
		this.sortorder = sortorder;
	}

	public String value() {
		return value;
	}

	public Integer getSortorder() {
		return sortorder;
	}

	public static TradeOrderSide fromValue(String value) {
		for (final TradeOrderSide tradeSide : TradeOrderSide.values()) {
			if (tradeSide.value.equalsIgnoreCase(value)) {
				return tradeSide;
			}
		}
		throw new IllegalArgumentException(value);
	}

	public static TradeOrderSide fromName(String name) {

		for (final TradeOrderSide tradeSide : TradeOrderSide.values()) {
			if (tradeSide.name().equalsIgnoreCase(name)) {
				return tradeSide;
			}
		}
		throw new IllegalArgumentException(name);

	}
}
