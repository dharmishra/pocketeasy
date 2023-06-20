package com.pocketeasy.pocketeasy.repos;

import com.pocketeasy.pocketeasy.domain.NetworkUsers;
import com.pocketeasy.pocketeasy.domain.Orders;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrdersRepository extends JpaRepository<Orders, UUID> {

    Orders findFirstByNetworkUsers(NetworkUsers networkUsers);

    boolean existsByOrderDate(LocalDateTime orderDate);

    boolean existsByOrderItemIgnoreCase(String orderItem);

}
