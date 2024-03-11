package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.request.SignInRequest;
import com.codigo.examen.request.SignUpRequest;
import com.codigo.examen.response.AutenticationResponse;
import com.codigo.examen.service.JWTService;
import com.codigo.examen.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSignUpUser() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("username");
        signUpRequest.setTelefono("telefono");
        signUpRequest.setEmail("email");
        signUpRequest.setPassword("password");
        signUpRequest.setRoleName("ROLE_USER");

        Rol rol = new Rol();
        rol.setNombreRol("ROLE_USER");

        when(rolRepository.findByNombreRol("ROLE_USER")).thenReturn(Optional.of(rol));

        Usuario usuario = new Usuario();
        usuario.setUsername("username");
        usuario.setTelefono("telefono");
        usuario.setEmail("email");
        usuario.setPassword("password");

        when(rolRepository.findByNombreRol(anyString())).thenReturn(Optional.of(new Rol())); // Mock para rolRepository
        when(usuarioService.createUsuario(any())).thenReturn(ResponseEntity.ok(usuario)); // Mock para usuarioService

        Usuario usuarioCreado = authenticationService.signUpUser(signUpRequest);

        assertNotNull(usuarioCreado);
        assertEquals("username", usuarioCreado.getUsername());
        assertEquals("telefono", usuarioCreado.getTelefono());
        assertEquals("email", usuarioCreado.getEmail());

    }
    @Test
    void testSignUpAdmin() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("username");
        signUpRequest.setTelefono("telefono");
        signUpRequest.setEmail("email");
        signUpRequest.setPassword("password");
        signUpRequest.setRoleName("ROLE_ADMIN");

        Rol rol = new Rol();
        rol.setNombreRol("ROLE_ADMIN");

        when(rolRepository.findByNombreRol("ROLE_ADMIN")).thenReturn(Optional.of(rol));

        Usuario usuario = new Usuario();
        usuario.setUsername("username");
        usuario.setTelefono("telefono");
        usuario.setEmail("email");
        usuario.setPassword("password");

        when(rolRepository.findByNombreRol(anyString())).thenReturn(Optional.of(new Rol())); // Mock para rolRepository
        when(usuarioService.createUsuario(any())).thenReturn(ResponseEntity.ok(usuario)); // Mock para usuarioService

        Usuario usuarioCreado = authenticationService.signUpUser(signUpRequest);

        assertNotNull(usuarioCreado);
        assertEquals("username", usuarioCreado.getUsername());
        assertEquals("telefono", usuarioCreado.getTelefono());
        assertEquals("email", usuarioCreado.getEmail());

    }
    @Test
    void testSignin() {
        // Configuración de datos de prueba
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("username");
        signInRequest.setPassword("password");

        // Configurar el comportamiento del mock
        Usuario usuario = new Usuario();
        usuario.setUsername("username");
        usuario.setPassword(new BCryptPasswordEncoder().encode("password"));

        when(usuarioRepository.findByUsername("username")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("dummy_token");

        // Llamar al método bajo prueba
        AutenticationResponse result = authenticationService.signin(signInRequest);

        // Verificaciones
        assertNotNull(result);
        assertEquals("dummy_token", result.getToken());
    }








}