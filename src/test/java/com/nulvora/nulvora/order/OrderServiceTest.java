package com.nulvora.nulvora.order;

import com.nulvora.nulvora.coupon.entity.Coupon;
import com.nulvora.nulvora.coupon.entity.CouponType;
import com.nulvora.nulvora.coupon.entity.DiscountType;
import com.nulvora.nulvora.coupon.repository.CouponRepository;
import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.order.dto.request.OrderRequest;
import com.nulvora.nulvora.order.dto.response.OrderCheckoutResponse;
import com.nulvora.nulvora.order.dto.response.OrderResponse;
import com.nulvora.nulvora.order.entity.Order;
import com.nulvora.nulvora.order.mapper.OrderMapper;
import com.nulvora.nulvora.order.repository.OrderRepository;
import com.nulvora.nulvora.order.service.OrderService;
import com.nulvora.nulvora.orderItens.dto.request.OrderItemRequest;
import com.nulvora.nulvora.orderItens.entity.OrderItem;
import com.nulvora.nulvora.orderItens.service.OrderItemService;
import com.nulvora.nulvora.user.entity.User;
import com.nulvora.nulvora.userCoupon.entity.UserCoupon;
import com.nulvora.nulvora.userCoupon.service.UserCouponService;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private GlobalHelperService globalHelperService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserCouponService userCouponService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldApplyPercentageCouponSuccessfully() throws StripeException {

        // Arrange

        OrderItemRequest itemRequest = new OrderItemRequest(1L, 2);

        OrderRequest request = new OrderRequest(
                List.of(itemRequest),
                "DESCONTO10"
        );

        User user = new User();

        OrderItem orderItem = mock(OrderItem.class);

        when(orderItem.getSubtotal())
                .thenReturn(new BigDecimal("100.00"));

        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setCode("DESCONTO10");
        coupon.setCouponType(CouponType.SHIPPING);
        coupon.setDiscountType(DiscountType.PERCENTAGE);
        coupon.setDiscountValue(new BigDecimal("100"));
        coupon.setMinOrderValue(new BigDecimal("50.00"));

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUsageCount(0);
        userCoupon.setUsageLimit(5);

        OrderResponse expectedResponse = mock(OrderResponse.class);

        when(globalHelperService.getLoggedUser())
                .thenReturn(user);

        when(orderItemService.createOrderItem(
                any(Order.class),
                eq(itemRequest)
        )).thenReturn(orderItem);

        when(couponRepository.findByCode("DESCONTO10"))
                .thenReturn(Optional.of(coupon));

        when(userCouponService.validateUseCoupon(1L))
                .thenReturn(userCoupon);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(orderMapper.toOrderResponse(any(Order.class)))
                .thenReturn(expectedResponse);

        // Act

        OrderCheckoutResponse result = orderService.createOrder(request);

        // Assert

        ArgumentCaptor<Order> captor =
                ArgumentCaptor.forClass(Order.class);

        verify(orderRepository).save(captor.capture());

        Order savedOrder = captor.getValue();

        assertSame(expectedResponse, result);

        System.out.println("Subtotal: " + savedOrder.getSubTotal());
        System.out.println("Frete: " + savedOrder.getShippingCost());
        System.out.println("Total: " + savedOrder.getTotal());

        assertEquals(
                0,
                new BigDecimal("100.00").compareTo(savedOrder.getSubTotal())
        );

        assertEquals(
                0,
                new BigDecimal("100.00").compareTo(savedOrder.getTotal())
        );

        assertEquals(1, userCoupon.getUsageCount());

        verify(userCouponService).validateUseCoupon(coupon.getId());

        verify(orderRepository).save(any(Order.class));
    }
}
