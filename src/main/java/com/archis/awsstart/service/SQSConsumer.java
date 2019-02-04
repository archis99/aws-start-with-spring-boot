/**
 * 
 */
package com.archis.awsstart.service;

import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author Archisman
 *
 */
@Component
public class SQSConsumer {

	static final String QUEUE_NAME = "https://sqs.us-east-2.amazonaws.com/728056408073/int-sqs-start";
	
	@SqsListener(QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Received message:"+ message);
        
    }
}
