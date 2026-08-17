package br.com.abrantes.MoneyControl.config;

import br.com.abrantes.MoneyControl.dto.request.LoginRequest;
import br.com.abrantes.MoneyControl.dto.request.RegisterRequest;
import br.com.abrantes.MoneyControl.dto.response.TokenResponseDTO;
import br.com.abrantes.MoneyControl.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterRequest registerRequest) throws Exception {
        authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(
            @RequestBody @Valid LoginRequest request
    ) {
        return ResponseEntity.ok(
                authenticationService.login(request)
        );
    }
}
