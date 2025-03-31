package com.proyectofinal.products_service.controller;

import com.proyectofinal.products_service.model.Product;
import com.proyectofinal.products_service.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Value("${server.port}")
    private int serverPort;

    @PostMapping("/save")
    public ResponseEntity<String> saveProduct(@RequestBody Product product) {
        productService.saveProduct(product);
        return ResponseEntity.ok().body("Producto guardado exitosamente.");
    }

    @GetMapping("/{code}")
    public ResponseEntity<Product> findProductByCode(@PathVariable Long code) {
        System.out.println("---------------- Estoy en el puerto " + serverPort);
        return ResponseEntity.ok().body(productService.findProductByCode(code));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok().body(productService.getAllProducts());
    }
}
