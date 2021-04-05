package com.aitrades.blockchain.trade.client;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import org.web3j.crypto.Credentials;
import org.web3j.tuples.generated.Tuple3;

import com.aitrades.blockchain.trade.dex.contract.EthereumDexContract;
import com.aitrades.blockchain.trade.domain.price.Cryptonator;
import com.aitrades.blockchain.trade.domain.price.EthPrice;
import com.aitrades.blockchain.trade.domain.price.PairPrice;
import com.fasterxml.jackson.databind.ObjectReader;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@Service
public class DexBinanceSmartChainPriceClient implements DexSubGraphPriceClient {

	private static final String BNB_USD_PRICE = "https://api.cryptonator.com/api/ticker/bnb-usd";

	@Resource(name="bscPriceHttpClient")
	public CloseableHttpClient closeableHttpClient;
	
	@Resource(name="cryptonatorObjectReader")
	private ObjectReader cryptonatorObjectReader;
	
	@Resource(name = "web3jBscServiceClient")
	private Web3jServiceClient web3jServiceClient;

	private Cryptonator getBNBPrice() throws Exception{
		try {
			return web3jServiceClient.getRestTemplate().getForEntity(BNB_USD_PRICE, Cryptonator.class).getBody();
		} catch (RestClientException e) {
		}
		return null;
	}

	public Tuple3<BigInteger, BigInteger, BigInteger> getReserves(String pairAddress, Credentials credentials) throws Exception{
		EthereumDexContract dexContract = new EthereumDexContract(pairAddress, 
																  web3jServiceClient.getWeb3j(), 
															      credentials);
		return dexContract.getReserves()
						  .flowable()
				          .subscribeOn(Schedulers.io())
				          .blockingSingle();
	}

	public BigDecimal calculatePrice(String pairAddress, Credentials credentials) throws Exception {
		Tuple3<BigInteger, BigInteger, BigInteger> reserves  =  getReserves(pairAddress, credentials);
		String price = getBNBPrice().getTicker().getPrice();
		if(StringUtils.isBlank(price)) {
			return null;
		}
		return BigDecimal.valueOf( Double.valueOf(1)/ reserves.component1().divide(reserves.component2()).doubleValue()).multiply(new BigDecimal(price));
	}
	

	@Override
	public String getResourceUrl(String route) {
		return null;
	}
	
	@Override
	public BigDecimal getPriceOfTicker(String pairAddress, Credentials credentials) throws Exception {
		return calculatePrice(pairAddress, credentials);
	}

	@Override
	public BigDecimal calculateTickerPrice(PairPrice pairPrice, EthPrice ethPrice) throws Exception {
		return null;
	}

	@Override
	public Flowable<PairPrice> getPairData(String pairAddress) throws Exception {
		
		return null;
	}

	@Override
	public Flowable<EthPrice> getEthPrice() throws Exception {
		return null;
	}
}