package com.aitrades.blockchain.trade.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)	
@JsonInclude(Include.NON_NULL)
public class BlockchainExchange {

	private String id;
	private String blockchainName;
	private String version;
	private String exchangeName;
	private String nativeCoinTicker;
	private Integer code;
	
	@JsonIgnore
	private boolean enabled;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBlockchainName() {
		return blockchainName;
	}
	public void setBlockchainName(String blockchainName) {
		this.blockchainName = blockchainName;
	}
	public String getExchangeName() {
		return exchangeName;
	}
	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}
	
	public String getNativeCoinTicker() {
		return nativeCoinTicker;
	}
	public void setNativeCoinTicker(String nativeCoinTicker) {
		this.nativeCoinTicker = nativeCoinTicker;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	@JsonIgnore
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
