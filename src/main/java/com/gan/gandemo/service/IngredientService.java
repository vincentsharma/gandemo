package com.gan.gandemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gan.gandemo.entity.Order;
import com.gan.gandemo.entity.Pizza;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer.Vanilla.std;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;

@Service
public class IngredientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    @Autowired
    private  ResourceLoader resourceLoader;

    private static Map<String, Integer> ingredients = new HashMap<String, Integer>();

    @PostConstruct
    public void init() throws JsonProcessingException {
        try {
            Resource resource = resourceLoader.getResource("classpath:ingredients.csv");
            InputStream in = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                String[] lineStr = line.toString().split(",");
                ingredients.put(lineStr[0], new Integer(lineStr[1]));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.error(e.getStackTrace().toString());
        }
        LOGGER.info(ingredients.toString());
        System.out.println(ingredients.toString());

        Map<String, Integer> ingredients = new HashMap<String, Integer>();
        ingredients.put("cheese", 1);
        Pizza pizza = new Pizza();
        pizza.setIngredients(ingredients);

        Pizza pizza2 = new Pizza();
        pizza2.setIngredients(ingredients);

        Order order = new Order();
        ArrayList<Pizza> pizzas = new ArrayList<>();
        pizzas.add(pizza);
        pizzas.add(pizza2);
        order.setPizzas(pizzas);

        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        String jsonString = mapper.writeValueAsString(order);
        System.out.println(jsonString);
    }

    private void useIngredient(String ingredient, Integer quantity) {
        Integer quantityAvailable = ingredients.get(ingredient);
        ingredients.put(ingredient, quantityAvailable - quantity);
        LOGGER.info("Used " + quantity + " " + ingredient + " " + ingredients.get(ingredient) + " remaining");
    }

    private String checkIngredients(Map<String, Integer> ingredientList) {
        for (Map.Entry<String, Integer> ingredientFromList : ingredientList.entrySet()) {
            if (ingredients.containsKey(ingredientFromList.getKey()) && ingredients.get(ingredientFromList.getKey()) <= 0) return ingredientFromList.getKey(); //Error condition identifies which ingredient is missing.
        }
        return null;
    }

    public synchronized String useIngredients(List<Pizza> pizzas) {
        for (Pizza pizza: pizzas) {
            //Check quantities
            String checkIngredientsReturn = checkIngredients(pizza.getIngredients());
            if (checkIngredientsReturn != null) return checkIngredientsReturn;
        }
        for (Pizza pizza: pizzas) {
            for (Map.Entry<String, Integer> ingredientFromList : pizza.getIngredients().entrySet()) {
                useIngredient(ingredientFromList.getKey(), ingredientFromList.getValue());
            }
        }
        return null; //No return indicates all ingredients were available and used
    }

//    public synchronized String useIngredients(Map<String, Integer> ingredientList) {
//        //Check quantities
//        String checkIngredientsReturn = checkIngredients(ingredientList);
//        if (checkIngredientsReturn != null) return checkIngredientsReturn;
//
//        for (Map.Entry<String, Integer> ingredientFromList : ingredientList.entrySet()) {
//            useIngredient(ingredientFromList.getKey(), ingredientFromList.getValue());
//        }
//        return null; //No return indicates all ingredients were available
//    }
}
