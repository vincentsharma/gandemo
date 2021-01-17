package com.gan.gandemo.service;


import com.gan.gandemo.entity.Order;
import com.gan.gandemo.entity.Oven;
import com.gan.gandemo.entity.Pizza;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


@Service
public class OrderHandlerService {

    @Autowired
    IngredientService ingredientService;

    @Autowired
    ApplicationContext ctx;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderHandlerService.class);

    public String processOrder(Order order) {
        List<Pizza> pizzas = order.getPizzas();

        //Use ingredients
        String useResult = ingredientService.useIngredients(pizzas);
        if (useResult != null) return useResult;

        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ctx.getBean("taskExecutor");

        ExecutorServiceAdapter adapter = new ExecutorServiceAdapter(taskExecutor);

        Collection<Oven> ovens = new ArrayList<>();
//        for (Pizza pizza : pizzas) {
//            Oven oven = ctx.getBean(Oven.class);
//            oven.setName("Oven");
//            taskExecutor.execute(oven);
//        }

        ArrayList<CompletableFuture<Oven>> oFuture = new ArrayList<>();

        for (Pizza pizza : pizzas) {
            Oven oven = ctx.getBean(Oven.class);
            oven.setName("Oven");
            ovens.add(oven);
            oFuture.add(CompletableFuture.supplyAsync( () -> oven, taskExecutor));
            taskExecutor.execute(oven);
        }

        for (int i = 0; i < oFuture.size(); i++)
        {
            oFuture.get(i).join();
        }
        
        return null;

    }

}
