package com.gan.gandemo.controller;

import com.gan.gandemo.entity.Order;
import com.gan.gandemo.service.OrderHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

@RestController
public class FluxController {

    @Autowired
    OrderHandlerService orderHandlerService;

    @GetMapping("/order")
    public Flux<Integer> returnFlux() {
        return Flux.just(1,2,3,4).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/orderstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> returnFluxStream() {
        return Flux.just(1,2,3,4).delayElements(Duration.ofSeconds(1)).log();
    }

    @PostMapping("/mono")
    public Mono<String> returnFlux2(@RequestBody Order order) {
        System.out.println(order);
        return Mono.just(orderHandlerService.processOrder((Order)order));
    }

    @PostMapping(value = "/order")
    public Mono<Order> orderPost2(@RequestBody Order order) {
        System.out.println(order);
        orderHandlerService.processOrder(order);
        return Mono.just(order);
    }

    @PostMapping(value = "/order2")
    public Order orderPost(@RequestBody Order order) {
        System.out.println(order);
        orderHandlerService.processOrder(order);
        return order;
    }

}
