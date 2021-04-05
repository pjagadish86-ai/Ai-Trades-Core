package com.aitrades.blockchain.trade.client;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.domain.TradeConstants;

@Service
public class DexSubGraphPriceFactoryClient {


	@Autowired
	private DexSubGraphPriceServiceClient dexSubGraphPriceServiceClient;
	
	@Autowired
	private DexBinanceSmartChainPriceClient dexBinanceSmartChainPriceClient;
	
	@Autowired
	private Map<String, DexSubGraphPriceClient> typesMap;

	@PostConstruct
	public void init() {
		typesMap.put(TradeConstants.UNISWAP, dexSubGraphPriceServiceClient);
		typesMap.put(TradeConstants.SUSHI, dexSubGraphPriceServiceClient);
		typesMap.put(TradeConstants.PANCAKE, dexBinanceSmartChainPriceClient);
	}
	
	public DexSubGraphPriceClient getRoute(String condition) {
		return typesMap.get(condition);
	}

}