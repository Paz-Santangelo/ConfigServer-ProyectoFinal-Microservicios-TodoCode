package com.proyectofinal.shoppingcart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDTO {
    private Long id;
    private Double totalPrice;
    private List<ProductDTO> productDTOList;
}
