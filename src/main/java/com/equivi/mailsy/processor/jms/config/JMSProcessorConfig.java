package com.equivi.mailsy.processor.jms.config;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JMSProcessorConfig {

//    @Value("${jms.broker.config}")
//    private String jmsConfigBrokerUrl;

    @Bean(name = "activemq")
    public ActiveMQComponent buildActiveMQComponent() {
        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setBrokerURL("tcp://localhost:61616");

        return activeMQComponent;
    }

}
