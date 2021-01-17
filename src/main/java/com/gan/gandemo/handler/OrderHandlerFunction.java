package com.gan.gandemo.handler;

import com.gan.gandemo.entity.Order;
import com.gan.gandemo.service.OrderHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OrderHandlerFunction {

    @Autowired
    OrderHandlerService orderHandlerService;

    public Mono<ServerResponse> flux(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Flux.just(1,2,3,4)
                        .log(),Integer.class
                );
    }

    public Mono<ServerResponse> mono(ServerRequest serverRequest) {
        final Mono<Order> order = serverRequest.bodyToMono(Order.class);
        String order2 = order.toString();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(1)
//                Flux.just(orderHandlerService.processOrder((Order)order))
                                .log(),Integer.class
                );
    }
}
