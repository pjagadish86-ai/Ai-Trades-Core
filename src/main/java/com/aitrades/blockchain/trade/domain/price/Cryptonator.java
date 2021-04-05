package com.aitrades.blockchain.trade.domain.price;

public class Cryptonator{
	
    private Ticker ticker;
    private int timestamp;
    private boolean success;
    private String error;
	
    public Cryptonator() {
	}
    
	public Ticker getTicker() {
		return ticker;
	}
	public void setTicker(Ticker ticker) {
		this.ticker = ticker;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public static class Ticker{
		private String base;
		private String target;
		private String price;
		private String volume;
		private String change;
		
		public Ticker() {
		}

		public String getBase() {
			return base;
		}
		public void setBase(String base) {
			this.base = base;
		}
		public String getTarget() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getVolume() {
			return volume;
		}
		public void setVolume(String volume) {
			this.volume = volume;
		}
		public String getChange() {
			return change;
		}
		public void setChange(String change) {
			this.change = change;
		}
	    
	}
    
}

