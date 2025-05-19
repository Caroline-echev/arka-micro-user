package com.arka.micro_user.adapters.driving.reactive.controller;


import com.arka.micro_user.adapters.driving.reactive.dto.request.AddressRequest;
import com.arka.micro_user.adapters.driving.reactive.mapper.IAddressDtoMapper;
import com.arka.micro_user.domain.api.IAddressServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
@Tag(name = "Address Controller", description = "Create a new address for a user")
public class AddressController {

    private final IAddressServicePort addressServicePort;
    private final IAddressDtoMapper addressDtoMapper;

    @PostMapping("/{dni}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new address for a user")
    public Mono<Void> createAddress(@Valid @RequestBody AddressRequest request, @PathVariable String dni) {
        return addressServicePort.createAddress(addressDtoMapper.toModel(request), dni);
    }
    @PutMapping("/{dni}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update an existing address for a user")
    public Mono<Void> updateAddress(@Valid @RequestBody AddressRequest request, @PathVariable String dni) {
        return addressServicePort.updateAddress(addressDtoMapper.toModel(request), dni);
    }

}

