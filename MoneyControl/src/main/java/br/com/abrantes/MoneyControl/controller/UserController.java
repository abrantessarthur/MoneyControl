package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.repository.UsersProjection;
import br.com.abrantes.MoneyControl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/page/{page}/size/{size}")
    public Page<UsersProjection> getAllUsersPageable(@PathVariable Integer page, @PathVariable Integer size){
        return userService.getAllUsuariosPageable(page, size);
    }
}
