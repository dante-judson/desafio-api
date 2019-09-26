package com.judson.desafio.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.judson.desafio.api.entity.Usuario;
import com.judson.desafio.api.repository.UsuarioRepository;
import com.judson.desafio.exception.BadCredentialsException;
import com.judson.desafio.exception.UserNotFoundException;
import com.judson.desafio.util.SecurityUtil;

@Service
public class AuthService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private SecurityUtil securityUtil;
	
	public String authenticate(@Valid Usuario usuario) throws BadCredentialsException, UserNotFoundException {
		
		Usuario usuarioBanco = usuarioRepository.findByUsername(usuario.getUsername());
		if(usuarioBanco == null) {
			throw new UserNotFoundException("Usuário não encontrado");
		}
		
		if(SecurityUtil.passwordMatch(usuario.getSenha(), usuarioBanco.getSenha())) {
			return securityUtil.generateToken(usuarioBanco);
		} else {
			throw new BadCredentialsException("Usuário ou senha incorretos");
		}
		
	}
}
