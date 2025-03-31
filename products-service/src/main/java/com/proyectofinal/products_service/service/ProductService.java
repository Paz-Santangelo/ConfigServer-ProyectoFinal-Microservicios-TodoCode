package com.proyectofinal.products_service.service;

import com.proyectofinal.products_service.model.Product;
import com.proyectofinal.products_service.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public void saveProduct(Product product) {
        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error de integridad al guardar el producto.", e);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Error al acceder a la base de datos al guardar el producto.", e);
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado al guardar el producto.", e);
        }
    }

    @Override
    public Product findProductByCode(Long code) {
        return productRepository.findById(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El producto con código " + code + " no fue encontrado."
                ));
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos al obtener todos los productos.", e);
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado al intentar obtener todos los productos.", e);
        }
    }
}
