package com.pocketeasy.pocketeasy.rest;

import com.pocketeasy.pocketeasy.model.OrdersDTO;
import com.pocketeasy.pocketeasy.service.OrdersService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/orderss", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrdersResource {

    private final OrdersService ordersService;

    public OrdersResource(final OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping
    public ResponseEntity<List<OrdersDTO>> getAllOrderss() {
        return ResponseEntity.ok(ordersService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdersDTO> getOrders(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(ordersService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createOrders(@RequestBody @Valid final OrdersDTO ordersDTO) {
        final UUID createdId = ordersService.create(ordersDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrders(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final OrdersDTO ordersDTO) {
        ordersService.update(id, ordersDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOrders(@PathVariable(name = "id") final UUID id) {
        ordersService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
