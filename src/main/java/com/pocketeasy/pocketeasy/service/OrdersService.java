package com.pocketeasy.pocketeasy.service;

import com.pocketeasy.pocketeasy.domain.NetworkUsers;
import com.pocketeasy.pocketeasy.domain.Orders;
import com.pocketeasy.pocketeasy.model.OrdersDTO;
import com.pocketeasy.pocketeasy.repos.NetworkUsersRepository;
import com.pocketeasy.pocketeasy.repos.OrdersRepository;
import com.pocketeasy.pocketeasy.util.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final NetworkUsersRepository networkUsersRepository;

    public OrdersService(final OrdersRepository ordersRepository,
            final NetworkUsersRepository networkUsersRepository) {
        this.ordersRepository = ordersRepository;
        this.networkUsersRepository = networkUsersRepository;
    }

    public List<OrdersDTO> findAll() {
        final List<Orders> orderss = ordersRepository.findAll(Sort.by("id"));
        return orderss.stream()
                .map(orders -> mapToDTO(orders, new OrdersDTO()))
                .toList();
    }

    public OrdersDTO get(final UUID id) {
        return ordersRepository.findById(id)
                .map(orders -> mapToDTO(orders, new OrdersDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final OrdersDTO ordersDTO) {
        final Orders orders = new Orders();
        mapToEntity(ordersDTO, orders);
        return ordersRepository.save(orders).getId();
    }

    public void update(final UUID id, final OrdersDTO ordersDTO) {
        final Orders orders = ordersRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ordersDTO, orders);
        ordersRepository.save(orders);
    }

    public void delete(final UUID id) {
        ordersRepository.deleteById(id);
    }

    private OrdersDTO mapToDTO(final Orders orders, final OrdersDTO ordersDTO) {
        ordersDTO.setId(orders.getId());
        ordersDTO.setOrderDate(orders.getOrderDate());
        ordersDTO.setOrderItem(orders.getOrderItem());
        ordersDTO.setPrice(orders.getPrice());
        ordersDTO.setStatus(orders.getStatus());
        ordersDTO.setNetworkUsers(orders.getNetworkUsers() == null ? null : orders.getNetworkUsers().getId());
        return ordersDTO;
    }

    private Orders mapToEntity(final OrdersDTO ordersDTO, final Orders orders) {
        orders.setOrderDate(ordersDTO.getOrderDate());
        orders.setOrderItem(ordersDTO.getOrderItem());
        orders.setPrice(ordersDTO.getPrice());
        orders.setStatus(ordersDTO.getStatus());
        final NetworkUsers networkUsers = ordersDTO.getNetworkUsers() == null ? null : networkUsersRepository.findById(ordersDTO.getNetworkUsers())
                .orElseThrow(() -> new NotFoundException("networkUsers not found"));
        orders.setNetworkUsers(networkUsers);
        return orders;
    }

    public boolean orderDateExists(final LocalDateTime orderDate) {
        return ordersRepository.existsByOrderDate(orderDate);
    }

    public boolean orderItemExists(final String orderItem) {
        return ordersRepository.existsByOrderItemIgnoreCase(orderItem);
    }

}
