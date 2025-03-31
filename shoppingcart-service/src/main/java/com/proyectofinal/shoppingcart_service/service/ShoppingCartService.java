package com.proyectofinal.shoppingcart_service.service;

import com.proyectofinal.shoppingcart_service.dto.ProductDTO;
import com.proyectofinal.shoppingcart_service.dto.ShoppingCartDTO;
import com.proyectofinal.shoppingcart_service.model.ShoppingCart;
import com.proyectofinal.shoppingcart_service.repository.IProductsAPI;
import com.proyectofinal.shoppingcart_service.repository.IShoppingCartRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartService implements IShoppingCartService {

    @Autowired
    private IShoppingCartRepository shoppingCartRepository;

    @Autowired
    private IProductsAPI productsAPI;

    @Override
    public void saveShoppingCart(List<Long> codesProducts) {
        try {
            List<ProductDTO> products = getProductsByCodes(codesProducts);

            if (products.isEmpty()) {
                throw new RuntimeException("No se pudo encontrar ningún producto válido para guardar en el carrito.");
            }

            ShoppingCart shoppingCart = new ShoppingCart();

            double total = products.stream()
                    .mapToDouble(ProductDTO::getUnitPrice)
                    .sum();

            shoppingCart.setTotalPrice(total);
            shoppingCart.setCodesProducts(codesProducts);

            shoppingCartRepository.save(shoppingCart);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Error al acceder a la base de datos al guardar el carrito de compras.", e);
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado al guardar el carrito de compras.", e);
        }
    }

    @CircuitBreaker(name = "products-service", fallbackMethod = "fallbackProductList")
    @Retry(name = "products-service")
    @Override
    public List<ProductDTO> getProductsByCodes(List<Long> codes) {
        List<ProductDTO> productsList = new ArrayList<>();
        for (Long code : codes) {
            ProductDTO product = productsAPI.findProductByCode(code);
            if (product != null) productsList.add(product);
        }
        return productsList;
    }

    public List<ProductDTO> fallbackProductList(List<Long> codes, Throwable t) {
        List<ProductDTO> fallbackList = new ArrayList<>();
        fallbackList.add(new ProductDTO(9999999L, "Falla", "Falla", 0.0));
        return fallbackList;
    }

    @Override
    public ShoppingCartDTO findShoppingCartById(Long id) {
        try {
            ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El carrito de compras con id " + id + " no fue encontrado."));
            List<ProductDTO> products = getProductsByCodes(shoppingCart.getCodesProducts());
            return new ShoppingCartDTO(shoppingCart.getId(), shoppingCart.getTotalPrice(), products);
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado al obtener el carrito de compras con id " + id, e);
        }
    }

    @Override
    public List<ShoppingCartDTO> getAllShoppingCarts() {
        try {
            List<ShoppingCart> shoppingCartsList = shoppingCartRepository.findAll();
            return shoppingCartsList.stream()
                    .map(shoppingCart -> new ShoppingCartDTO(shoppingCart.getId(), shoppingCart.getTotalPrice(), getProductsByCodes(shoppingCart.getCodesProducts())))
                    .toList();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos al obtener todos los carritos de compras.", e);
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado al intentar obtener todos los carritos de compras.", e);
        }
    }

}
