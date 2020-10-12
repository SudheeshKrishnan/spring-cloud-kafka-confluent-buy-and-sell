package com.techwording.scs;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.client.RestTemplate;



@EnableBinding(Sink.class)
public class Consumer {

	private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
	@Autowired
	RestTemplate rs;
	@Value("${url}")
	String Url;

	@StreamListener(target = Sink.INPUT)
	public void consume(String message) {

		logger.info("recieved a string message : " + message);
	}

	@StreamListener(target = Sink.INPUT, condition = "headers['type']=='chat'")
	public void handle(@Payload ChatMessage message) {

		String url = Url + message.getMode();
		logger.info("Url is : {}",url);
		
		
		
		
		Map<String, String> map = new HashMap<>();
		map.put("uid", message.getUid());
		
		OrderDataBean request = new OrderDataBean();
		request.setOrderType(message.getOrderType());
		request.setQuantity(message.getQuantity());
		request.setOrderStatus(message.getOrderStatus());
		request.setSymbol(message.getSymbol());
		request.setHoldingID(message.getHoldingID());

		rs.postForEntity(url, request, OrderDataBean.class, map);
		
		
		
		logger.info("recieved a complex message : {}", message.getOrderType());
		
		
		
		
		
		
		
		
		
		
	}

}
