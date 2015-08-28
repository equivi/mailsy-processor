package com.equivi.mailsy.processor.router;

import com.equivi.mailsy.processor.process.EmailChannelProcessor;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DeliveryChannelRouter extends SpringRouteBuilder {

    @Autowired
    private EmailChannelProcessor emailChannelProcessor;

//    @Value("${queue.console2processor}")
//    private String queueConsoleToProcessor;
//
//    @Value("${queue.processor2email}")
//    private String queueProcessorToEmailChannel;

    @Override
    public void configure() throws Exception {
        from("activemq:queue:Q_CONSOLE_TO_PROCESSOR")
                .convertBodyTo(Long.class)
                .pipeline()
                .process(emailChannelProcessor).marshal().json(JsonLibrary.Jackson).to("activemq:queue:Q_PROCESSOR_TO_EMAILCHANNEL");
    }
}
