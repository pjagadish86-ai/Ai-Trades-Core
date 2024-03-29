package com.aitrades.blockchain.trade.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.domain.BlockchainExchange;
import com.aitrades.blockchain.trade.domain.DexContractStaticCodeValue;
import com.aitrades.blockchain.trade.domain.TradeConstants;
import com.aitrades.blockchain.trade.repository.DexContractStaticCodeValueRepository;
import com.github.benmanes.caffeine.cache.Caffeine;

@Service
public class DexContractStaticCodeValuesService {
	
	private static final String BLOCKCHAIN_EXCHANGES = "BLOCKCHAIN_EXCHANGES";

	private static com.github.benmanes.caffeine.cache.Cache<String, Map<String, String>> dexContractAddressCache;
	
	private static com.github.benmanes.caffeine.cache.Cache<String, Map<String,String>> blockchainExchangeCache;

	@Autowired
	private DexContractStaticCodeValueRepository dexContractStaticCodeValueRepository;
	
	@Autowired
    private DexContractStaticCodeValuesService() {
        dexContractAddressCache = Caffeine.newBuilder()
	                               .expireAfterWrite(3, TimeUnit.HOURS)
	                               .build();
        blockchainExchangeCache = Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.HOURS)
                .build();
    }
	
	private Map<String, String> getDexContractAddress(String route){
        return dexContractAddressCache.get(route, this :: getStaticCodeValuesMap);
	}
	
	public String getDexContractAddress(String route, String type) {
		getDexContractAddress(route);
		return dexContractAddressCache.getIfPresent(route) != null ? dexContractAddressCache.getIfPresent(route).get(type) : null;
	}
	
	private Map<String, String> getStaticCodeValuesMap(String route) {
		List<DexContractStaticCodeValue> staticCodeValues = dexContractStaticCodeValueRepository.fetchAllDexContractRouterAndFactoryAddress();
		Map<String, String> contractAddress = new HashMap<>();	
		for(DexContractStaticCodeValue dexContractStaticCodeValue : staticCodeValues) {
			if(StringUtils.equalsIgnoreCase(route, dexContractStaticCodeValue.getDexName())) {
				contractAddress.put(TradeConstants.ROUTER, dexContractStaticCodeValue.getRouterAddress());
				contractAddress.put(TradeConstants.FACTORY, dexContractStaticCodeValue.getFactoryAddress());
				contractAddress.put(TradeConstants.WNATIVE, dexContractStaticCodeValue.getWrappedNativeAddress());
				return contractAddress;
			}
		}
		return null;
	}

	public String fetchNativeCoinTicker(String exchange) {
		String nativCoinTicker  = blockchainExchangeCache.getIfPresent(BLOCKCHAIN_EXCHANGES) != null ? blockchainExchangeCache.getIfPresent(BLOCKCHAIN_EXCHANGES).get(exchange) : null;
		if(StringUtils.isNotBlank(nativCoinTicker)) {
			return nativCoinTicker.toLowerCase();
		}
		List<BlockchainExchange> priceOracleNativeTickers =   fetchBlockChainExchanges();
		Map<String, String> exchangeToNativeTickerPairMap = new HashMap<>();
		for(BlockchainExchange  priceOracleNativeTicker: priceOracleNativeTickers) {
			exchangeToNativeTickerPairMap.put(priceOracleNativeTicker.getExchangeName(), priceOracleNativeTicker.getNativeCoinTicker());
		}
		blockchainExchangeCache.put(BLOCKCHAIN_EXCHANGES, exchangeToNativeTickerPairMap);
		nativCoinTicker  = blockchainExchangeCache.getIfPresent(BLOCKCHAIN_EXCHANGES) != null ? blockchainExchangeCache.getIfPresent(BLOCKCHAIN_EXCHANGES).get(exchange) : null;
		return nativCoinTicker.toLowerCase();
	}
	
	public List<BlockchainExchange> fetchBlockChainExchanges(){
		return  dexContractStaticCodeValueRepository.fetchSupportedBlockchains();
	}
	
}
