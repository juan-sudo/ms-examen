package com.codigo.examen.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import javax.management.relation.Role;

@Getter
@Setter
public class SignUpRequest {
    private String username;
    private String telefono;
    private String email;
    private String password;
    private String roleName;

}
//{
//        "username": "usuario",
//        "telefono": "123456789",
//        "email": "usuario@example.com",
//        "password": "contraseña",
//        "roleName": "ADMIN"
//        }