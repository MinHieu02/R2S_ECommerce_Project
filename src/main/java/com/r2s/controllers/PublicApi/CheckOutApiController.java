package com.r2s.controllers.PublicApi;

import com.r2s.dtos.OrderDTO;
import com.r2s.dtos.common.ResponseDTO;
import com.r2s.services.order.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckOutApiController {

    private final OrderServiceImpl orderService;

    @Autowired
    public CheckOutApiController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    @Transactional
    public ResponseEntity<ResponseDTO<OrderDTO>> createOrder(@RequestBody OrderDTO orderDTO) {
        ResponseDTO<OrderDTO> response = orderService.createOrder(orderDTO);
        HttpStatus httpStatus = response.getStatus().equals("404 NOT_FOUND") ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(response);
    }
}
