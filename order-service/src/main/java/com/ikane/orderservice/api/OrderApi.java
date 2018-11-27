package com.ikane.orderservice.api;

import com.ikane.orderservice.domain.Item;
import com.ikane.orderservice.domain.Order;
import com.ikane.orderservice.dto.order.CustomerOrderDetails;
import com.ikane.orderservice.dto.request.CustomerOrderRequest;
import com.ikane.orderservice.repository.OrderRepository;
import com.ikane.orderservice.service.ProductServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderApi {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductServiceProxy productServiceProxy;

    @GetMapping("/orders")
    public List<CustomerOrderDetails> getCustomerOrders(@RequestParam String customerId) {
        final List<Order> order = orderRepository.findByCustomerId(customerId);
        return order.stream().map(o -> toCustomerOrderDetails(o)).collect(Collectors.toList());
    }

    @GetMapping("/orders/{id}")
    public CustomerOrderDetails getOrders(@PathVariable("id") Long orderId) {
        final Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }
        return toCustomerOrderDetails(order);
    }

    private CustomerOrderDetails toCustomerOrderDetails(Order order) {
        return CustomerOrderDetails.builder()
                .orderId(order.getId())
                .createdDate(order.getCreatedDate())
                .externalReference(order.getExternalReference())
                .items(toItemList(order.getItems()))
                .build();
    }

    private List<com.ikane.orderservice.dto.product.Item> toItemList(List<Item> items) {
        return items.stream().map(item -> toItemDto(item)).collect(Collectors.toList());
    }

    private com.ikane.orderservice.dto.product.Item toItemDto(Item item) {
        return com.ikane.orderservice.dto.product.Item
                .builder()
                .product(productServiceProxy.getProduct(item.getProductId())).build();
    }

    @PostMapping("/orders")
    public Order save(@RequestBody CustomerOrderRequest request) {
        return orderRepository.save(Order
                .builder()
                .customerId(request.getCustomerId())
                .externalReference(request.getExternalReference())
                .items((request.getItems() == null) ? null : toItems(request.getItems())).build());
    }

    private List<Item> toItems(List<com.ikane.orderservice.dto.request.Item> items) {
        return items.stream().map(item -> Item.builder().productId(item.getProductId())
                .quantity(item.getQuantity()).build()).collect(Collectors.toList());
    }
}
