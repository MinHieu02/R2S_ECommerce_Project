package com.r2s.controllers.PublicApi;

import com.r2s.dtos.OrderDTO;
import com.r2s.dtos.OrderDetailDTO;
import com.r2s.dtos.OrderDetailsResponseDTO;
import com.r2s.dtos.OrderListResponseDTO;
import com.r2s.dtos.common.ResponseDTO;
import com.r2s.services.order.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderApiController {

    private final OrderServiceImpl orderService;

    @Autowired
    public OrderApiController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<List<OrderListResponseDTO>>> getAllOrders() {
        ResponseDTO<List<OrderListResponseDTO>> response = orderService.getAllOrders();
        HttpStatus httpStatus = response.getStatus().equals("404 NOT_FOUND") ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(response);
    }

    @GetMapping("/{orderId}/details")
    public ResponseEntity<ResponseDTO<List<OrderDetailDTO>>> getOrderDetailsByOrderId(@PathVariable Long orderId) {
        ResponseDTO<List<OrderDetailDTO>> response = orderService.getOrderDetailsByOrderId(orderId);
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(response.getStatus()));
        return ResponseEntity.status(httpStatus).body(response);
    }
}