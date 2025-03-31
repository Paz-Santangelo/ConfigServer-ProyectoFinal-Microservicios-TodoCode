package com.proyectofinal.shoppingcart_service.repository;

import com.proyectofinal.shoppingcart_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "products-service")
public interface IProductsAPI {
    @GetMapping("/products/{code}")
    public ProductDTO findProductByCode(@PathVariable("code") Long code);
}
