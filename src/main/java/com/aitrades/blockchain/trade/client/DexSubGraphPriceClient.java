package com.aitrades.blockchain.trade.client;

import java.math.BigDecimal;

import org.web3j.crypto.Credentials;

import com.aitrades.blockchain.trade.domain.price.EthPrice;
import com.aitrades.blockchain.trade.domain.price.PairPrice;

import io.reactivex.Flowable;

public interface DexSubGraphPriceClient {
	
	public String getResourceUrl(String route);
	
	public BigDecimal getPriceOfTicker(String pairAddress, Credentials credentials) throws Exception;
	
	public BigDecimal calculateTickerPrice(PairPrice pairPrice, EthPrice ethPrice) throws Exception;
	
	public Flowable<PairPrice> getPairData(final String pairAddress) throws Exception;
	
	public Flowable<EthPrice> getEthPrice() throws Exception;
	
}
