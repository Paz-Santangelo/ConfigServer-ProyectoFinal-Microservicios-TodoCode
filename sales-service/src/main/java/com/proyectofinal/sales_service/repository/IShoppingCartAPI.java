package com.proyectofinal.sales_service.repository;

import com.proyectofinal.sales_service.dto.ShoppingCartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "shoppingcart-service")
public interface IShoppingCartAPI {

    @GetMapping("/shoppingcart/{id}")
    public ShoppingCartDTO findShoppingCartById(@PathVariable("id") Long id);
}
