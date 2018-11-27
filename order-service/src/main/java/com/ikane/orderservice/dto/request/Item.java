package com.ikane.orderservice.dto.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Item {
    private Long productId;
    private BigDecimal quantity;
}
