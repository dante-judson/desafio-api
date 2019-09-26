package com.judson.desafio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.judson.desafio.api.entity.Usuario;
import com.judson.desafio.api.repository.UsuarioRepository;
import com.judson.desafio.util.SecurityUtil;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private SecurityUtil securityUtil;
	
	public Usuario findByUsername(String username) {
		return usuarioRepository.findByUsername(username);
	}
	
	public List<Usuario> findAll(){
		return usuarioRepository.findAll();
	}
	
	public Usuario createUsuario(Usuario usuario) {
		usuario.setSenha(securityUtil.encodePassword(usuario.getSenha()));
		Usuario usuarioCriado = usuarioRepository.save(usuario);
		usuarioCriado.setSenha(null);
		return usuarioCriado;
	}
	
}
