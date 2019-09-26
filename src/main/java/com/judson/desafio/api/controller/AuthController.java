package com.judson.desafio.api.controller;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.judson.desafio.api.entity.Usuario;
import com.judson.desafio.exception.BadCredentialsException;
import com.judson.desafio.exception.UserNotFoundException;
import com.judson.desafio.service.AuthService;
import com.judson.desafio.service.EmailService;
import com.judson.desafio.service.UsuarioService;

@RestController
public class AuthController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private EmailService emailService;

	@PostMapping("/sing-in")
	@PermitAll
	public ResponseEntity<JwtResponse> singIn(@RequestBody Usuario usuario)
			throws BadCredentialsException, UserNotFoundException {
		String token = authService.authenticate(usuario);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	@PostMapping("/sing-up")
	@PermitAll
	public ResponseEntity<Usuario> createUsuario(@RequestBody @Valid Usuario usuario) {
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.createUsuario(usuario));
	}
	
	@PostMapping("/email")
	@PermitAll
	public ResponseEntity sendEmail(@RequestBody String body, @RequestBody String to, @RequestBody String subject) {
		emailService.sendEmail(body, to, subject);
		return ResponseEntity.ok().build();
	}

//	@GetMapping("/teste2")
//	@RolesAllowed("LISTAR_USUARIOS")
//	public ResponseEntity<Long> getUsuarios2(@SessionAttribute(Usuario.USER_ID) Long userId) {
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return ResponseEntity.ok(userId);
//	}

	static class JwtResponse {
		String token;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public JwtResponse(String token) {
			this.token = token;
		}
	}
}
