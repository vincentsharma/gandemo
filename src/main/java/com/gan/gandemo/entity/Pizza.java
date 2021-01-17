package com.gan.gandemo.entity;

import lombok.Data;
import java.util.Map;

@Data
public class Pizza {
    private Map<String, Integer> ingredients = null;
}
