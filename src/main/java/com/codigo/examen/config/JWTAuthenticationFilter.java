package com.codigo.examen.config;

import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.service.JWTService;
import com.codigo.examen.service.UsuarioService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
//mvn clean install jacoco:prepare-agent jacoco:report
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UsuarioService usuarioService;
    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String autHeader = request.getHeader("Authorization");
        final String jwt;

        if (StringUtils.isEmpty(autHeader) || !StringUtils.startsWithIgnoreCase(autHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = autHeader.substring(7);
        try {
            final String username;
            Usuario usuario;
            username = jwtService.extractUserName(jwt);

            if (Objects.nonNull(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                usuario = usuarioService.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
                    logger.info("usuario e username:::"+usuario.getUsername());
                //UserDetails userDetails =usuarioService.userDetailService().loadUserByUsername(username);

                if (jwtService.validateToken(jwt, usuario)) {
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                    Set<GrantedAuthority> authorities = usuario.getRoles().stream()
                            .map(rol -> new SimpleGrantedAuthority(rol.getNombreRol()))
                            .collect(Collectors.toSet());

                    UserDetails userDetails = new User(usuario.getUsername(), usuario.getPassword(), authorities);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    securityContext.setAuthentication(authToken);
                    SecurityContextHolder.setContext(securityContext);
                }



            }
        } catch (ExpiredJwtException e) {
            logger.error("Token expired: {}", e.getMessage());
            // Manejar token caducado
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 No autorizado
            response.getWriter().write("¡Su sesión ha expirado! Inicie sesión nuevamente.");
        } catch (UsernameNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            // Manejar la excepción de Usuario no encontrado
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 No autorizado
            response.getWriter().write("Usuario no encontrado.");
        }
        filterChain.doFilter(request, response);
    }









}

