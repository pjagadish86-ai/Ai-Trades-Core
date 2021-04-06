package com.aitrades.blockchain.trade;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import com.aitrades.blockchain.trade.client.Web3jServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication(scanBasePackages = {"com.aitrades.blockchain"})
@EnableAsync
public class Application {
	
	private static final String ENDPOINT_WSS = "wss://eth-mainnet.ws.alchemyapi.io/v2/9XymsgNnaJBVR1KHUM6aH9dG2CU1FJ-2";
	private static final String BSC_ENDPOINT_WSS ="wss://holy-twilight-violet.bsc.quiknode.pro/9ccdc8c6748f92a972bc9c9c1b8b56de961c0fc6/";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean(name="web3jClient")
    public Web3j web3J() {
        return Web3j.build(webSocketService());
    }
	
	@Bean(name = "web3bscjClient")
	public Web3j web3bscjClient() {
		return Web3j.build(webBSCSocketService());
	}
	
	@Bean(name = "webBSCSocketService")
	public WebSocketService webBSCSocketService() {
		WebSocketService webSocketService = new WebSocketService(new WebSocketClient(parseURI(BSC_ENDPOINT_WSS)), false);
		try {
			webSocketService.connect();
		} catch (ConnectException e) {
			e.printStackTrace();
		}
		return webSocketService;
	}

	
	@Bean(name = "webSocketService")
	public WebSocketService webSocketService() {
		WebSocketService webSocketService = new WebSocketService(new WebSocketClient(parseURI(ENDPOINT_WSS)), false);
		try {
			webSocketService.connect();
		} catch (ConnectException e) {
			e.printStackTrace();
		}
		return webSocketService;
	}
	
	private static URI parseURI(String serverUrl) {
        try {
            return new URI(serverUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(String.format("Failed to parse URL: '%s'", serverUrl), e);
        }
    }
    
    @Bean(name="web3jServiceClient")
    public Web3jServiceClient web3jServiceClient(@Qualifier("web3jClient") final Web3j web3j, final ObjectMapper objectMapper) {
		return new Web3jServiceClient(web3j, restTemplate(), objectMapper);
    }
    
	@Bean(name = "web3jBscServiceClient")
	public Web3jServiceClient web3jBscServiceClient(@Qualifier("web3bscjClient") final Web3j web3j,
												 final ObjectMapper objectMapper) {
		return new Web3jServiceClient(web3j, restTemplate(), objectMapper);
	}
	
    @Bean
    public RestTemplate restTemplate() {
    	return new RestTemplate();
    }
    
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	//TODO: async rabitamq
	@Bean(name = "orderSubmitRabbitTemplate")
	public AmqpTemplate postorderRabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
	
	@Bean(name ="graphHqlPriceHttpClient")
	public CloseableHttpClient uniswapPriceHttpClient() {
		return HttpClients.createMinimal();	
	}
    
	@Bean(name ="bscPriceHttpClient")
	public CloseableHttpClient bscPriceHttpClient() {
		return HttpClients.createMinimal();	
	}
}
