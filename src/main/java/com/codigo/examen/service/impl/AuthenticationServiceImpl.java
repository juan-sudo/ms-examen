package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.request.SignInRequest;
import com.codigo.examen.request.SignUpRequest;
import com.codigo.examen.response.AutenticationResponse;
import com.codigo.examen.service.AuthenticationService;
import com.codigo.examen.service.JWTService;
import com.codigo.examen.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final RolRepository rolRepository;
    private final UsuarioService usuarioService;

    @Override
    public Usuario signUpUser(SignUpRequest signUpRequest) {
        Usuario usuario=new Usuario();
        usuario.setUsername(signUpRequest.getUsername());
        usuario.setTelefono(signUpRequest.getTelefono());
        usuario.setEmail(signUpRequest.getEmail());
        Set<Rol> roles = new HashSet<>();
        Rol rol = rolRepository.findByNombreRol(signUpRequest.getRoleName())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        roles.add(rol);

        usuario.setRoles(roles);
        usuario.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));

        ResponseEntity<Usuario> responseEntity = usuarioService.createUsuario(usuario);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Error al crear el usuario");
        }
    }

//    @Override
//    public Usuario signUpUser(SignUpRequest signUpRequest) {
//        Usuario usuario=new Usuario();
//        usuario.setUsername(signUpRequest.getUsername());
//        usuario.setTelefono(signUpRequest.getTelefono());
//        usuario.setEmail(signUpRequest.getEmail());
//        Set<Rol> roles = new HashSet<>();
//        Rol rol = rolRepository.findByNombreRol(signUpRequest.getRoleName())
//                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
//        roles.add(rol);
//
//        usuario.setRoles(roles);
//        usuario.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
//
//        return usuarioRepository.save(usuario);
//    }

    @Override
    public Usuario signUpAdmin(SignUpRequest signUpRequest) {
        Usuario usuario=new Usuario();
        usuario.setUsername(signUpRequest.getUsername());
        usuario.setTelefono(signUpRequest.getTelefono());
        usuario.setEmail(signUpRequest.getEmail());
        Set<Rol> roles = new HashSet<>();
        Rol rol = rolRepository.findByNombreRol(signUpRequest.getRoleName())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        roles.add(rol);

        usuario.setRoles(roles);
        usuario.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));

        return usuarioRepository.save(usuario);
    }

    @Override
    public AutenticationResponse signin(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getUsername(),signInRequest.getPassword()));
        var user = usuarioRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no valido"));

        var jwt = jwtService.generateToken(user);
        AutenticationResponse authenticationResponse =  new AutenticationResponse();
        authenticationResponse.setToken(jwt);
        return authenticationResponse;
    }

}
