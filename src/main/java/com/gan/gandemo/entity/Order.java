package com.gan.gandemo.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Order {
    private ArrayList<Pizza> pizzas;
}
