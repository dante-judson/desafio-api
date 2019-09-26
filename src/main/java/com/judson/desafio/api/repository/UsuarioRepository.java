package com.judson.desafio.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.judson.desafio.api.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Usuario findByUsername(String username);

}
