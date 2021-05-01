package com.aitrades.blockchain.trade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aitrades.blockchain.trade.domain.EndpointConfig;
import com.aitrades.blockchain.trade.repository.EndpointConfigRepository;

@Service
public class EndpointConfigServiceResolver {

	@Autowired
	private EndpointConfigRepository endpointConfigRepository;
	
	public List<EndpointConfig> fetchEndpointConfigs(){
		return  endpointConfigRepository.fetchSupportedBlockchainEndpointNodeConfigUrls();
	}
	
}
