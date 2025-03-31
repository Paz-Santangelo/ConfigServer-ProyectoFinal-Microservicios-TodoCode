package com.proyectofinal.shoppingcart_service.controller;

import com.proyectofinal.shoppingcart_service.dto.ShoppingCartDTO;
import com.proyectofinal.shoppingcart_service.service.IShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @PostMapping("/save")
    public ResponseEntity<String> saveShoppingCart(@RequestBody List<Long> codesProducts) {
        shoppingCartService.saveShoppingCart(codesProducts);
        return ResponseEntity.ok().body("Carrito de compras guardado correctamente.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCartDTO> findShoppingCartById(@PathVariable Long id) {
        return ResponseEntity.ok().body(shoppingCartService.findShoppingCartById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ShoppingCartDTO>> getAllShoppingCarts() {
        return ResponseEntity.ok().body(shoppingCartService.getAllShoppingCarts());
    }
}
