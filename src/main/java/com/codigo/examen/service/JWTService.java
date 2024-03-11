package com.codigo.examen.service;

import com.codigo.examen.entity.Usuario;

public interface JWTService {
    String generateToken(Usuario usuario);

    boolean validateToken(String token, Usuario usuario);


    String extractUserName(String token);

}
