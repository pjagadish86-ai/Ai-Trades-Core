package com.aitrades.blockchain.trade.client;

import java.math.BigDecimal;

import org.web3j.crypto.Credentials;

import com.aitrades.blockchain.trade.domain.price.Cryptonator;

public interface DexSubGraphPriceClient {
	
	public Cryptonator nativeCoinPrice(String route) throws Exception;
	
	public BigDecimal tokenPrice(String pairAddress, String route, Credentials credentials) throws Exception;
}
