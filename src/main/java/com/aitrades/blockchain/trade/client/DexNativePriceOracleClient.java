package com.aitrades.blockchain.trade.client;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.tuples.generated.Tuple3;

import com.aitrades.blockchain.trade.dex.contract.EthereumDexContract;
import com.aitrades.blockchain.trade.domain.TradeConstants;
import com.aitrades.blockchain.trade.domain.price.Cryptonator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.collect.ImmutableMap;

import io.reactivex.schedulers.Schedulers;

@Service
public class DexNativePriceOracleClient implements DexSubGraphPriceClient {

	private static final String BNB_USD_PRICE = "https://api.cryptonator.com/api/ticker/bnb-usd";
	
	private static final String ETH_USD_PRICE = "https://api.cryptonator.com/api/ticker/eth-usd";

	
	private static final Map<String, String> BLOCKCHAIN_NATIVE_PRICE_ORACLE = ImmutableMap.of(TradeConstants.UNISWAP, ETH_USD_PRICE, TradeConstants.SUSHI, ETH_USD_PRICE, TradeConstants.PANCAKE, BNB_USD_PRICE);

	@Resource(name="bscPriceHttpClient")
	public CloseableHttpClient closeableHttpClient;
	
	@Resource(name="cryptonatorObjectReader")
	private ObjectReader cryptonatorObjectReader;
	
	@Autowired
	private Web3jServiceClientFactory web3jServiceClientFactory;

	private Tuple3<BigInteger, BigInteger, BigInteger> getReserves(String pairAddress, String route,  Credentials credentials) {
		EthereumDexContract dexContract = new EthereumDexContract(pairAddress, 
																  web3jServiceClientFactory.getWeb3jMap().get(route).getWeb3j(), 
															      credentials);
		return dexContract.getReserves()
						  .flowable()
				          .subscribeOn(Schedulers.io())
				          .blockingSingle();
	}

	@Override
	public Cryptonator nativeCoinPrice(String route) throws Exception {
		return web3jServiceClientFactory.getWeb3jMap().get(route).restTemplate.getForEntity(BLOCKCHAIN_NATIVE_PRICE_ORACLE.get(route), Cryptonator.class).getBody();
	}

	@Override
	public BigDecimal tokenPrice(String pairAddress, String route, Credentials credentials) throws Exception {
		Tuple3<BigInteger, BigInteger, BigInteger> reserves  =  getReserves(pairAddress, route, credentials);
		String price = nativeCoinPrice(route).getTicker().getPrice();
		if(StringUtils.isBlank(price)) {
			return null;
		}
		return BigDecimal.valueOf( Double.valueOf(1)/ reserves.component1().divide(reserves.component2()).doubleValue()).multiply(new BigDecimal(price));
	}

}