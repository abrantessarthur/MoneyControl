package br.com.abrantes.MoneyControl.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank
        String name
) {
}
