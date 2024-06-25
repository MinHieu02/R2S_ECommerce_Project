package com.r2s.services.order;

import com.r2s.dtos.OrderDTO;
import com.r2s.dtos.OrderDetailDTO;
import com.r2s.dtos.OrderDetailsResponseDTO;
import com.r2s.dtos.OrderListResponseDTO;
import com.r2s.dtos.common.ResponseDTO;
import com.r2s.entities.OrderDetailEntity;
import com.r2s.entities.OrderEntity;
import com.r2s.entities.ProductEntity;
import com.r2s.repositories.OrderRepository;
import com.r2s.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public ResponseDTO<OrderDTO> createOrder(OrderDTO orderDTO) {
        try {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setTotal(orderDTO.getTotal());
            orderEntity.setOrdersDay(new Date());

            List<OrderDetailEntity> orderDetails = new ArrayList<>();

            if (orderDTO.getDetails() != null) {
                for (OrderDetailDTO itemDTO : orderDTO.getDetails()) {
                    OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                    ProductEntity productEntity = productRepository.findById(itemDTO.getProduct().getId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    orderDetailEntity.setProduct(productEntity);
                    orderDetailEntity.setQuantity(itemDTO.getQuantity());
                    orderDetailEntity.setUnitPrice(itemDTO.getUnitPrice());
                    orderDetailEntity.setOrder(orderEntity);

                    orderDetails.add(orderDetailEntity);
                }
            }

            orderEntity.setOrderDetails(orderDetails);

            OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
            OrderDTO savedOrderDTO = modelMapper.map(savedOrderEntity, OrderDTO.class);

            return ResponseDTO.<OrderDTO>builder()
                    .status(String.valueOf(HttpStatus.CREATED.value()))
                    .message("CREATE_SUCCESS_MESSAGE")
                    .data(savedOrderDTO)
                    .build();
        } catch (Exception e) {
            return ResponseDTO.<OrderDTO>builder()
                    .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .message("CREATE_FAILED_MESSAGE")
                    .build();
        }
    }

    @Transactional
    public ResponseDTO<List<OrderListResponseDTO>> getAllOrders() {
        try {
            List<OrderEntity> orderEntities = orderRepository.findAll();
            List<OrderListResponseDTO> orderListResponseDTOS = orderEntities.stream()
                    .map(orderEntity -> modelMapper.map(orderEntity, OrderListResponseDTO.class))
                    .collect(Collectors.toList());

            return ResponseDTO.<List<OrderListResponseDTO>>builder()
                    .status(String.valueOf(HttpStatus.OK.value()))
                    .message("FETCH_SUCCESS_MESSAGE")
                    .data(orderListResponseDTOS)
                    .build();
        } catch (Exception e) {
            return ResponseDTO.<List<OrderListResponseDTO>>builder()
                    .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .message("FETCH_FAILED_MESSAGE")
                    .build();
        }
    }

    @Transactional
    public ResponseDTO<List<OrderDetailDTO>> getOrderDetailsByOrderId(Long orderId) {
        try {
            // Find the order by id
            OrderEntity orderEntity = orderRepository.findById(orderId)D
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Map order details to DTOs
            List<OrderDetailDTO> orderDetailDTOs = orderEntity.getOrderDetails().stream()
                    .map(orderDetailEntity -> modelMapper.map(orderDetailEntity, OrderDetailDTO.class))
                    .collect(Collectors.toList());

            return ResponseDTO.<List<OrderDetailDTO>>builder()
                    .status(String.valueOf(HttpStatus.OK.value()))
                    .message("FETCH_ORDER_DETAILS_SUCCESS_MESSAGE")
                    .data(orderDetailDTOs)
                    .build();
        } catch (Exception e) {
            return ResponseDTO.<List<OrderDetailDTO>>builder()
                    .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .message("FETCH_ORDER_DETAILS_FAILED_MESSAGE")
                    .build();
        }
    }

}
