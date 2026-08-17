package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.config.TokenProvider;
import br.com.abrantes.MoneyControl.dto.request.LoginRequest;
import br.com.abrantes.MoneyControl.dto.request.RegisterRequest;
import br.com.abrantes.MoneyControl.dto.response.TokenResponseDTO;
import br.com.abrantes.MoneyControl.entity.RolesEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.enums.RoleTypeEnum;
import br.com.abrantes.MoneyControl.exception.BadRequestException;
import br.com.abrantes.MoneyControl.repository.RolesRepository;
import br.com.abrantes.MoneyControl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    @Value("${jwt.expiration:900000}")
    private long expirationTime;

    public void register(RegisterRequest registerRequest) throws BadRequestException {
        UserEntity user = userRepository.findByEmail(registerRequest.email())
                .orElse(null);
        if (user != null) {
            throw new BadRequestException("E-mail already in use");
        }
        RolesEntity role = rolesRepository.findByName(RoleTypeEnum.ROLE_USER.name())
                .orElseGet(()-> rolesRepository.save(RolesEntity.builder().name(RoleTypeEnum.ROLE_USER.name()).build()));

        UserEntity newUser = UserEntity.builder()
                .username(registerRequest.username())
                .email(registerRequest.email())
                .roles(Set.of(role))
                .password(passwordEncoder.encode(registerRequest.password()))
                .build();
        userRepository.save(newUser);
    }

    public TokenResponseDTO login(LoginRequest login) throws BadRequestException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.email(), login.password()));
            String token = tokenProvider.generateToken(authentication);

            return new TokenResponseDTO(token, expirationTime);
        } catch (BadCredentialsException e) {
            throw new BadRequestException("E-mail or password invalids");
        }
    }




}
