package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.dto.request.CreateCreditCard;
import br.com.abrantes.MoneyControl.dto.response.CreditCardResponse;
import br.com.abrantes.MoneyControl.service.CreditCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class CreditCardController {
    private final CreditCardService creditCardService;

    @PostMapping
    public ResponseEntity<CreditCardResponse> create(@Valid @RequestBody CreateCreditCard create, Authentication authentication){
        CreditCardResponse response = creditCardService.create(create, authentication);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        creditCardService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
