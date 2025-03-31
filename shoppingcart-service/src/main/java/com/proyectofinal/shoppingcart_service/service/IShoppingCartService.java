package com.proyectofinal.shoppingcart_service.service;

import com.proyectofinal.shoppingcart_service.dto.ProductDTO;
import com.proyectofinal.shoppingcart_service.dto.ShoppingCartDTO;
import com.proyectofinal.shoppingcart_service.model.ShoppingCart;

import java.util.List;

public interface IShoppingCartService {

    public void saveShoppingCart(List<Long> codesProducts);

    public List<ProductDTO> getProductsByCodes(List<Long> codes);

    public ShoppingCartDTO findShoppingCartById(Long id);

    public List<ShoppingCartDTO> getAllShoppingCarts();

}
