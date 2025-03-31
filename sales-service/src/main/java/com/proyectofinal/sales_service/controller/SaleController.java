package com.proyectofinal.sales_service.controller;

import com.proyectofinal.sales_service.dto.SaleDTO;
import com.proyectofinal.sales_service.model.Sale;
import com.proyectofinal.sales_service.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private ISaleService saleService;

    @PostMapping("/save")
    public ResponseEntity<String> saveSale(@RequestBody Sale sale) {
        saleService.saveSale(sale.getDate(), sale.getIdShoppingCart());
        return ResponseEntity.ok().body("Venta realizada con éxito.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> findSaleById(@PathVariable Long id) {
        return ResponseEntity.ok().body(saleService.findSaleById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        return ResponseEntity.ok().body(saleService.getAllSales());
    }
}
