package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class CreditCardController {
    private final CreditCardService creditCardService;


}
