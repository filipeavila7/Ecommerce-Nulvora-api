package com.nulvora.nulvora.order.controller;

import com.nulvora.nulvora.order.dto.request.OrderRequest;
import com.nulvora.nulvora.order.dto.response.OrderCheckoutResponse;
import com.nulvora.nulvora.order.dto.response.OrderResponse;
import com.nulvora.nulvora.order.service.OrderService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/new")
    public ResponseEntity<OrderCheckoutResponse> createOrder(
            @Valid @RequestBody OrderRequest request
    ) throws StripeException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(request));
    }
}
