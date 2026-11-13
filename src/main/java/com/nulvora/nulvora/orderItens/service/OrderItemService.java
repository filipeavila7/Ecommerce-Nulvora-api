package com.nulvora.nulvora.orderItens.service;

import com.nulvora.nulvora.order.entity.Order;
import com.nulvora.nulvora.orderItens.dto.request.OrderItemRequest;
import com.nulvora.nulvora.orderItens.entity.OrderItem;
import com.nulvora.nulvora.orderItens.repository.OrderItemRepository;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import com.nulvora.nulvora.productVariant.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductVariantRepository productVariantRepository;

    // criar ordemItem
    public OrderItem createOrderItem(Order order, OrderItemRequest item){
        // pega a variante
        ProductVariant productVariant = productVariantRepository.findByIdAndActiveTrue(item.productVariantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Variante não encontrada"));

        // cria a orderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductVariant(productVariant);
        orderItem.setPrice(productVariant.getPrice());
        orderItem.setQuantity(item.quantity());

        // calcular subtotal
        BigDecimal subtotal = productVariant.getPrice().multiply(BigDecimal.valueOf(item.quantity()));
        orderItem.setSubtotal(subtotal);

        // relacionamente inverso
        order.getOrderItems().add(orderItem);

        return orderItem;
    }
}
