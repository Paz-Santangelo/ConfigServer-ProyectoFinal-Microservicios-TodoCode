package com.proyectofinal.sales_service.service;

import com.proyectofinal.sales_service.dto.SaleDTO;
import com.proyectofinal.sales_service.dto.ShoppingCartDTO;
import com.proyectofinal.sales_service.model.Sale;
import com.proyectofinal.sales_service.repository.ISaleRepository;
import com.proyectofinal.sales_service.repository.IShoppingCartAPI;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class SaleService implements ISaleService {

    @Autowired
    private ISaleRepository saleRepository;

    @Autowired
    private IShoppingCartAPI shoppingCartAPI;

    @Override
    public void saveSale(LocalDate date, Long idShoppingCart) {
        try {
            ShoppingCartDTO shoppingCartDto = findShoppingCartById(idShoppingCart);
            Sale sale = new Sale();
            sale.setDate(date);
            sale.setIdShoppingCart(shoppingCartDto.getId());

            saleRepository.save(sale);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Error al acceder a la base de datos al guardar la venta.", e);
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado al guardar la venta.", e);
        }
    }

    @Override
    public SaleDTO findSaleById(Long id) {
        try {
            Sale sale = saleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la venta con id " + id));
            ShoppingCartDTO shoppingCartDTO = findShoppingCartById(sale.getIdShoppingCart());
            return new SaleDTO(
                    sale.getId(),
                    sale.getDate(),
                    shoppingCartDTO
            );
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado al intentar obtener la venta con id " + id, e);
        }
    }

    @Override
    public List<SaleDTO> getAllSales() {
        try {
            List<Sale> salesList = saleRepository.findAll();
            return salesList.stream()
                    .map(sale -> new SaleDTO(sale.getId(), sale.getDate(), findShoppingCartById(sale.getIdShoppingCart()))).toList();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al acceder a la base de datos al obtener todas las ventas.", e);
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado al intentar obtener todas las ventas.", e);
        }
    }

    @CircuitBreaker(name = "shoppingcart-service", fallbackMethod = "fallbackFindShoppingCartById")
    @Retry(name = "shoppingcart-service")
    public ShoppingCartDTO findShoppingCartById(Long id) {
        return shoppingCartAPI.findShoppingCartById(id);
    }

    public ShoppingCartDTO fallbackFindShoppingCartById(Long id, Throwable throwable) {
        return new ShoppingCartDTO(id, 0.0, null);
    }
}
