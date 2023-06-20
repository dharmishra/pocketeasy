package com.pocketeasy.pocketeasy.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NetworkCompanyDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String companyName;

    private String description;

    private Boolean active;

    private List<UUID> subscriptions;

}
