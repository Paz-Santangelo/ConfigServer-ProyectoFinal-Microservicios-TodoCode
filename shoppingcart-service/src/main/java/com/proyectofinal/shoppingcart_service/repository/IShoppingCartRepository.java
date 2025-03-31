package com.proyectofinal.shoppingcart_service.repository;

import com.proyectofinal.shoppingcart_service.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}
