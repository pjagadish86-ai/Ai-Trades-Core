package com.aitrades.blockchain.trade.client;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.domain.TradeConstants;

@Service
public class Web3jServiceClientFactory {

	@Resource(name = "web3jServiceClient")
	private Web3jServiceClient web3jServiceClient;
	
	@Resource(name = "web3jBscServiceClient")
	private Web3jServiceClient web3jBscServiceClient;
	
	private static final Map<String, Web3jServiceClient> WEB3J_MAP = new HashMap<>();
	
	@PostConstruct
	private void post() {
		WEB3J_MAP.put(TradeConstants.UNISWAP, web3jServiceClient);
		WEB3J_MAP.put(TradeConstants.SUSHI, web3jServiceClient);
		WEB3J_MAP.put(TradeConstants.PANCAKE, web3jBscServiceClient);

	}

	public Map<String, Web3jServiceClient> getWeb3jMap() {
		return WEB3J_MAP;
	}
	
	
}
