package com.gan.gandemo.entity;

import com.gan.gandemo.service.OrderHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Oven extends Thread {

    @Value("${orderTimeInSeconds}")
    private Integer orderTimeInSeconds;

    private static final Logger LOGGER = LoggerFactory.getLogger(Oven.class);

    @Override
    public void run() {
        try {
            Thread.sleep(orderTimeInSeconds * 1000);
            LOGGER.info("Pizza done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
