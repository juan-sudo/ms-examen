package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.service.JWTService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JWTServiceImpl implements JWTService {
    private static final Logger logger = LoggerFactory.getLogger(JWTServiceImpl.class);


    @Override
    public String generateToken(Usuario usuario) {
        Claims claims = Jwts.claims().setSubject(usuario.getUsername());

        claims.put("roles", usuario.getRoles());
        return Jwts.builder()
                .setClaims(claims)
                //.setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+3600000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();


    }

    private Key getSignKey(){
        byte[] key= Decoders.BASE64.decode("85732b878c0f544da4a863804775ef3914e8ccb82b08820a278302c5b826e291");
        return Keys.hmacShaKeyFor(key);

    }






    @Override
    public boolean validateToken(String token, Usuario usuario) {
        final String username = extractUserName(token);
        return username.equals(usuario.getUsername()) && !isTokenExpired(token) && rolesMatch(token, usuario);

    }
    private boolean rolesMatch(String token, Usuario usuario) {
        Claims claims = Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
        List<Map<String, String>> rolesList = claims.get("roles", List.class);
        Set<String> roles = rolesList.stream().map(role -> role.get("nombreRol")).collect(Collectors.toSet());
        Set<String> usuarioRoles = usuario.getRolesNames();
        return roles.containsAll(usuarioRoles);
    }
//    private boolean rolesMatch(String token, Usuario usuario) {
//        Claims claims = Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
//        List<String> rolesList = claims.get("roles", List.class);
//        if (rolesList == null) {
//            return false; // O realizar alguna acción adecuada para manejar el caso en que no haya roles
//        }
//        Set<String> roles = rolesList.stream().map(String::trim).collect(Collectors.toSet());
//        Set<String> usuarioRoles = usuario.getRolesNames();
//        return roles.containsAll(usuarioRoles);
//    }


//    private boolean rolesMatch(String token, Usuario usuario) {
//        Claims claims = Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
//        List<String> rolesList = claims.get("roles", List.class);
//        Set<String> roles = new HashSet<>(rolesList); // Convertir la lista a un conjunto
//        Set<String> usuarioRoles = new HashSet<>(usuario.getRolesNames());
//        return roles.containsAll(usuarioRoles);
//    }



//    private boolean rolesMatch(String token, Usuario usuario) {
//        Claims claims = Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
//        List<String> rolesList = claims.get("roles", List.class);
//        Set<String> roles = new HashSet<>(rolesList);
//        Set<String> usuarioRoles = usuario.getRolesNames();
//        return roles.containsAll(usuarioRoles);
//    }




    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        try {
            final Claims claims=extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (NullPointerException | JwtException e) {
            // Manejar la excepción, por ejemplo, retornar null o lanzar una excepción personalizada
            logger.error("Error al procesar el token JWT: {}", e.getMessage());
            return null;
        }
    }
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            // El token ha expirado, puedes manejarlo de la forma que desees
            logger.error("El token ha expirado: {}", e.getMessage());

            return null;
        }
    }



    @Override
    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }
}
