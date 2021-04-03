package com.aitrades.blockchain.trade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = { "com.aitrades.blockchain.trade.repository"})
public class OrderMongoConfig extends AbstractReactiveMongoConfiguration {
	
	private static final String ORDER = "order";

	@Override
	protected String getDatabaseName() {
		return ORDER;
	}

	@Bean(name = "orderMongoTemplate")
	public ReactiveMongoTemplate buyOrderMongoTemplate(com.mongodb.reactivestreams.client.MongoClient client) {
		return new ReactiveMongoTemplate(client, getDatabaseName());
	}
}