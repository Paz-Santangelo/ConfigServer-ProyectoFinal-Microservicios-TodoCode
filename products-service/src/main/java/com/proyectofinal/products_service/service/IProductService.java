package com.proyectofinal.products_service.service;

import com.proyectofinal.products_service.model.Product;

import java.util.List;

public interface IProductService {

    public void saveProduct(Product product);

    public Product findProductByCode(Long code);

    public List<Product> getAllProducts();

}
