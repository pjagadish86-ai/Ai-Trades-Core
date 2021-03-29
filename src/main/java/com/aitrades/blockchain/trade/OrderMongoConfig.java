package com.aitrades.blockchain.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = { "com.aitrades.blockchain.trade.repository"})
public class OrderMongoConfig extends AbstractReactiveMongoConfiguration {
	
	@Autowired
	private com.mongodb.reactivestreams.client.MongoClient client;

	@Override
	protected String getDatabaseName() {
		return "order";
	}

	@Bean(name = "orderMongoTemplate")
	public ReactiveMongoTemplate buyOrderMongoTemplate() {
		return new ReactiveMongoTemplate(client, getDatabaseName());
	}
}