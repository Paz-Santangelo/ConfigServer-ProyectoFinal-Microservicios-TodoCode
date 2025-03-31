package com.proyectofinal.sales_service.service;

import com.proyectofinal.sales_service.dto.SaleDTO;

import java.time.LocalDate;
import java.util.List;

public interface ISaleService {

    public void saveSale(LocalDate date, Long idShoppingCart);

    public SaleDTO findSaleById(Long id);

    public List<SaleDTO> getAllSales();
}
