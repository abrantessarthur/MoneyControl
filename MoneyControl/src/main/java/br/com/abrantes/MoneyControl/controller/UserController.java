package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.repository.UsersProjection;
import br.com.abrantes.MoneyControl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    public ResponseEntity<Void> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    public Page<UsersProjection> getAllUsersPageable(@PathVariable Integer page, @PathVariable Integer size){
        return userService.getAllUsuariosPageable(page, size);
    }
}
