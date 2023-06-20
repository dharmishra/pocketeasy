package com.pocketeasy.pocketeasy.rest;

import com.pocketeasy.pocketeasy.model.NetworkCompanyDTO;
import com.pocketeasy.pocketeasy.service.NetworkCompanyService;
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
@RequestMapping(value = "/api/networkCompanys", produces = MediaType.APPLICATION_JSON_VALUE)
public class NetworkCompanyResource {

    private final NetworkCompanyService networkCompanyService;

    public NetworkCompanyResource(final NetworkCompanyService networkCompanyService) {
        this.networkCompanyService = networkCompanyService;
    }

    @GetMapping
    public ResponseEntity<List<NetworkCompanyDTO>> getAllNetworkCompanys() {
        return ResponseEntity.ok(networkCompanyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NetworkCompanyDTO> getNetworkCompany(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(networkCompanyService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createNetworkCompany(
            @RequestBody @Valid final NetworkCompanyDTO networkCompanyDTO) {
        final UUID createdId = networkCompanyService.create(networkCompanyDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateNetworkCompany(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final NetworkCompanyDTO networkCompanyDTO) {
        networkCompanyService.update(id, networkCompanyDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteNetworkCompany(@PathVariable(name = "id") final UUID id) {
        networkCompanyService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
