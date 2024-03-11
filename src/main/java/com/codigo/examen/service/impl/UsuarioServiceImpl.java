package com.codigo.examen.service.impl;

import com.codigo.examen.config.JWTAuthenticationFilter;
import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private static final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);



    public UserDetailsService userDetailService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Usuario usuario = usuarioRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
                Set<GrantedAuthority> authorities = usuario.getRoles().stream()
                        .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombreRol()))
                        .collect(Collectors.toSet());

                return new User(usuario.getUsername(), usuario.getPassword(), authorities);
            }
        };
    }




    @Override
    public ResponseEntity<Usuario> createUsuario(Usuario usuario) {
        Optional<Usuario> existingUser = usuarioRepository.findByUsername(usuario.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        return getUsuarioResponseEntity(usuario);
    }

    @Override
    public ResponseEntity<Usuario> getUsuarioById(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Usuario> updateUsuario(Long id, Usuario usuario) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);
        if (existingUsuario.isPresent()) {
            usuario.setIdUsuario(id);

            log.info("usuario que llega en request::"+usuario.getTelefono());
            log.info("usuario id::"+usuario.getUsername());
            log.info("usuario que llega en request::"+existingUsuario.get().getUsername());





            if (!usuario.getUsername().equals(existingUsuario.get().getUsername())) {
                Optional<Usuario> userWithNewUsername = usuarioRepository.findByUsername(usuario.getUsername());

                if (userWithNewUsername.isPresent()) {
                    return ResponseEntity.badRequest().body(null);
                }
            }
            return getUsuarioResponseEntity(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    {
//        "username":"pepe1",
//            "telefono": "11111111",
//            "email":"tomate@gmail.com",
//            "password":"1234"
//    }
    private ResponseEntity<Usuario> getUsuarioResponseEntity(Usuario usuario) {
        Set<Rol> assignedRoles = new HashSet<>();
        for (Rol roles : usuario.getRoles()) {
            Optional<Rol> rol = rolRepository.findById(roles.getIdRol());
            if (!rol.isPresent()) {
                return ResponseEntity.badRequest().body(null);
            }
            assignedRoles.add(rol.get());
        }
        usuario.setRoles(assignedRoles);
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(updatedUsuario);
    }

    @Override
    public ResponseEntity<Usuario> deleteUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuarioRepository.delete(usuario.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
}
