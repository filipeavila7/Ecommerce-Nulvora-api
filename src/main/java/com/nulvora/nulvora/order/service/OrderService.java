package com.nulvora.nulvora.order.service;

import com.nulvora.nulvora.coupon.entity.Coupon;
import com.nulvora.nulvora.coupon.repository.CouponRepository;
import com.nulvora.nulvora.coupon.service.CouponService;
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
import com.nulvora.nulvora.userCoupon.entity.UserCoupon;
import com.nulvora.nulvora.userCoupon.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final GlobalHelperService globalHelperService;
    private final OrderItemService orderItemService;
    private final OrderMapper orderMapper;
    private final CouponRepository couponRepository;
    private final UserCouponService userCouponService;

    // TODO - fazer integração com api de cálculo de frete
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

        // valor padrão de frete por enquanto
        order.setShippingCost(BigDecimal.TEN);

        // inicia o subtotal (preço base antes dos descontos)
        BigDecimal subtotal = BigDecimal.ZERO;

        // percorre os intens e suas quatidades
        for (var itens : request.items()){
            OrderItem orderItem = orderItemService.createOrderItem(order, itens);

            subtotal = subtotal.add(orderItem.getSubtotal());
        }

        // adciona o valor base sem descontos e adcionais
        order.setSubTotal(subtotal);

        // valor final
        order.setTotal(subtotal.add(order.getShippingCost()));

        // caso tenha cupom aplicado
        if (request.couponCode() != null && !request.couponCode().isBlank()){
            System.out.println("entrou nessa merda ");
            Coupon coupon = couponRepository.findByCode(request.couponCode())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Cupom não encontrado"));

            // valida se o cupom de desconto é aplicável e aplica
            applyCoupon(order, coupon);
        }

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    // validar se o cupom pode ser aplicado
    private UserCoupon validateCouponAplication(Order order, Coupon coupon) {
        // pega a relação e ja valida o cupom
        UserCoupon userCoupon = userCouponService.validateUseCoupon(coupon.getId());

        // verifica se o valor mínimo da order não foi atingido
        if (order.getSubTotal().compareTo(coupon.getMinOrderValue()) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O valor mínimo para utilizar esse cupom não foi atingido."
            );
        }

        // verifica se o usuario execedeu o limite de ultilização do cupom
        if (userCoupon.getUsageCount() >= userCoupon.getUsageLimit()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Limite de utilização do cupom atingido."
            );
        }

        return userCoupon;
    }

    // aplicar desconto
    private void applyCoupon(Order order, Coupon coupon) {
        // valida aplicação do cupom
        UserCoupon userCoupon = validateCouponAplication(order, coupon);

        // caso para cada tipo de cupom e calcula usando o metodo
        BigDecimal discount = switch (coupon.getCouponType()) {
            case PRODUCT -> calculateDiscount(order.getSubTotal(), coupon)
                    .min(order.getSubTotal());

            case SHIPPING -> calculateDiscount(order.getShippingCost(), coupon)
                    .min(order.getShippingCost());

            case ORDER, FIRST_PURCHASE -> calculateDiscount(order.getTotal(), coupon)
                    .min(order.getSubTotal());
        };

        System.out.println("Tipo do cupom: " + coupon.getCouponType());
        System.out.println("Tipo do desconto: " + coupon.getDiscountType());
        System.out.println("Valor configurado: " + coupon.getDiscountValue());
        System.out.println("Subtotal: " + order.getSubTotal());
        System.out.println("Frete: " + order.getShippingCost());
        System.out.println("Desconto calculado: " + discount);

        // Aplicação do desconto
        discount = discount.min(order.getTotal());
        order.setTotal(order.getTotal().subtract(discount));

        // gasta um uso do cupom
        userCoupon.setUsageCount(userCoupon.getUsageCount() +1);

        // seta o desconto na order
        order.setDiscount(discount);

        // seta o cupom na order
        order.setCoupon(coupon);


        System.out.println("Desconto salvo no objeto: " + order.getDiscount());
        System.out.println("Total após desconto: " + order.getTotal());
    }

    // calcular desconto dependendo do tipo do desconto
    private BigDecimal calculateDiscount(BigDecimal baseValue, Coupon coupon) {
        return switch (coupon.getDiscountType()) {
            case PERCENTAGE -> baseValue
                    .multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            case FIXED_AMOUNT -> coupon.getDiscountValue().min(baseValue);
        };
    }
}
