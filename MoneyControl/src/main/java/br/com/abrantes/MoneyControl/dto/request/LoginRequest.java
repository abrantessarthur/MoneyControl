package br.com.abrantes.MoneyControl.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email
        @NotNull
        String email,
        @NotBlank
        String password
) {
}
