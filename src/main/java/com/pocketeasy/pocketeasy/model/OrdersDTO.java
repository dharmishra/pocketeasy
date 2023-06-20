package com.pocketeasy.pocketeasy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrdersDTO {

    private UUID id;

    @NotNull
    private LocalDateTime orderDate;

    @NotNull
    @Size(max = 255)
    private String orderItem;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "75.08")
    private BigDecimal price;

    private Status status;

    private UUID networkUsers;

}
