package br.com.abrantes.MoneyControl;

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
import br.com.abrantes.MoneyControl.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RolesRepository rolesRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private TokenProvider tokenProvider;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(
                userRepository, rolesRepository, passwordEncoder,
                authenticationManager, tokenProvider
        );
        ReflectionTestUtils.setField(authenticationService, "expirationTime", 900000L);
    }

    // ---------- register ----------

    @Test
    @DisplayName("Deve registrar usuário quando email não existe e role já existe")
    void deveRegistrarUsuarioComRoleExistente() throws BadRequestException {
        RegisterRequest request = new RegisterRequest("joao@email.com", "senha123", "joao");
        RolesEntity roleUser = RolesEntity.builder().name(RoleTypeEnum.ROLE_USER.name()).build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(rolesRepository.findByName(RoleTypeEnum.ROLE_USER.name())).thenReturn(Optional.of(roleUser));
        when(passwordEncoder.encode(request.password())).thenReturn("hash_senha123");

        authenticationService.register(request);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository, times(1)).save(captor.capture());

        UserEntity salvo = captor.getValue();
        assertEquals("joao", salvo.getDisplayName());
        assertEquals("joao@email.com", salvo.getEmail());
        assertEquals("hash_senha123", salvo.getPassword());
        assertTrue(salvo.getRoles().contains(roleUser));

        // não deve criar role nova, já que ela existia
        verify(rolesRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve criar a role ROLE_USER quando ela ainda não existir")
    void deveCriarRoleQuandoNaoExistir() throws BadRequestException {
        RegisterRequest request = new RegisterRequest("maria@email.com", "senha123", "maria");
        RolesEntity roleNova = RolesEntity.builder().name(RoleTypeEnum.ROLE_USER.name()).build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(rolesRepository.findByName(RoleTypeEnum.ROLE_USER.name())).thenReturn(Optional.empty());
        when(rolesRepository.save(any(RolesEntity.class))).thenReturn(roleNova);
        when(passwordEncoder.encode(request.password())).thenReturn("hash_senha123");

        authenticationService.register(request);

        verify(rolesRepository, times(1)).save(any(RolesEntity.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Não deve registrar quando email já está em uso")
    void naoDeveRegistrarComEmailDuplicado() {
        RegisterRequest request = new RegisterRequest("joao@email.com", "senha123", "joao");
        UserEntity existente = UserEntity.builder().email("joao@email.com").build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(existente));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> authenticationService.register(request));

        assertEquals("E-mail already in use", ex.getMessage());
        verify(userRepository, never()).save(any());
        verify(rolesRepository, never()).findByName(any());
    }

    // ---------- login ----------

    @Test
    @DisplayName("Deve autenticar e retornar token quando credenciais são válidas")
    void deveLogarComSucesso() throws BadRequestException {
        LoginRequest request = new LoginRequest("joao@email.com", "senha123");
        Authentication authenticationMock = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);
        when(tokenProvider.generateToken(authenticationMock)).thenReturn("jwt-token-123");

        TokenResponseDTO response = authenticationService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token-123", response.token());
        assertEquals(900000L, response.expiresIn());
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando credenciais são inválidas")
    void naoDeveLogarComCredenciaisInvalidas() {
        LoginRequest request = new LoginRequest("joao@email.com", "senhaErrada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> authenticationService.login(request));

        assertEquals("E-mail or password invalid", ex.getMessage());
        verify(tokenProvider, never()).generateToken(any());
    }
}
