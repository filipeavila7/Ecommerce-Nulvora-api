package com.nulvora.nulvora.order.service;

import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.order.dto.request.OrderRequest;
import com.nulvora.nulvora.order.dto.response.OrderResponse;
import com.nulvora.nulvora.order.entity.Order;
import com.nulvora.nulvora.order.entity.OrderStatus;
import com.nulvora.nulvora.order.mapper.OrderMapper;
import com.nulvora.nulvora.order.repository.OrderRepository;
import com.nulvora.nulvora.orderItens.entity.OrderItem;
import com.nulvora.nulvora.orderItens.service.OrderItemService;
import com.nulvora.nulvora.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final GlobalHelperService globalHelperService;
    private final OrderItemService orderItemService;
    private final OrderMapper orderMapper;


    // TODO - criar logica de cupons e calcular total da order
    // TODO - testar essa service, response etc
    // TODO - criar tests para testar comportamento dos cupons
    // TODO - criar logica de calculo de frete e dar um jeito de mostrar na response
    // TODO - criar metodo pra validar se pode aplicar o cupom
    // TODO - tratar duplicados no caso de mandar 2 variantes iguais para uma order (o banco ja trata mas é seguraça extra)
    @Transactional
    public OrderResponse createOrder(OrderRequest request){
        User loggedUser = globalHelperService.getLoggedUser();

        // cria o objeto de order
        Order order = new Order();
        order.setUser(loggedUser);
        order.setStatus(OrderStatus.PENDING);

        // inicia o subtotal (preço base antes dos descontos)
        BigDecimal subtotal = BigDecimal.ZERO;

        // percorre os intens e suas quatidades
        for (var itens : request.items()){
            OrderItem orderItem = orderItemService.createOrderItem(order, itens);

            subtotal = subtotal.add(orderItem.getSubtotal());
        }

        // adciona o valor base sem descontos
        order.setSubTotal(subtotal);

        // TODO : criar logica de desconto por cupons calculo de frete
        // Total = Subtotal - Discount + ShippingCost
        order.setTotal(subtotal);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }
}
