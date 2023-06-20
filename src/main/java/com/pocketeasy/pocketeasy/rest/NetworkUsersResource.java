package com.pocketeasy.pocketeasy.rest;

import com.pocketeasy.pocketeasy.model.NetworkUsersDTO;
import com.pocketeasy.pocketeasy.service.NetworkUsersService;
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
@RequestMapping(value = "/api/networkUserss", produces = MediaType.APPLICATION_JSON_VALUE)
public class NetworkUsersResource {

    private final NetworkUsersService networkUsersService;

    public NetworkUsersResource(final NetworkUsersService networkUsersService) {
        this.networkUsersService = networkUsersService;
    }

    @GetMapping
    public ResponseEntity<List<NetworkUsersDTO>> getAllNetworkUserss() {
        return ResponseEntity.ok(networkUsersService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NetworkUsersDTO> getNetworkUsers(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(networkUsersService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createNetworkUsers(
            @RequestBody @Valid final NetworkUsersDTO networkUsersDTO) {
        final UUID createdId = networkUsersService.create(networkUsersDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateNetworkUsers(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final NetworkUsersDTO networkUsersDTO) {
        networkUsersService.update(id, networkUsersDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteNetworkUsers(@PathVariable(name = "id") final UUID id) {
        networkUsersService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
