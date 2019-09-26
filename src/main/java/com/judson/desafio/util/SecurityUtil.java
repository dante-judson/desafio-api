package com.judson.desafio.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.judson.desafio.api.entity.Permissao;
import com.judson.desafio.api.entity.Usuario;
import com.judson.desafio.exception.UnauthorizedUserException;
import com.judson.desafio.service.UsuarioService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class SecurityUtil {

	@Value("${jwtkey}")
	private String jwtKey;

	@Autowired
	private UsuarioService usuarioService;

	public String encodePassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	@SuppressWarnings("deprecation")
	public String generateToken(Usuario usuario) {
		usuario.setSenha(null);
		try {
			return Jwts.builder().signWith(SignatureAlgorithm.HS256, jwtKey.getBytes("UTF-8"))
					.addClaims(toClaims(usuario)).compact();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private Map<String, Object> toClaims(Usuario usuario) {
		Map<String, Object> claims = new HashMap<>();
		Field[] fields = usuario.getClass().getDeclaredFields();

		for (Field field : fields) {
			try {
				field.setAccessible(true);
				claims.put(field.getName(), field.get(usuario));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return claims;
	}

	public static boolean passwordMatch(String rawPassword, String encoded) {
		return BCrypt.checkpw(rawPassword, encoded);
	}

	public Usuario getUsuarioFromToken(String token) throws UnauthorizedUserException {
		try {
			return usuarioService.findByUsername(getUsernameFromToken(token));
		} catch (Exception e) {
			throw new UnauthorizedUserException("Token inválido.");
		}
	}

	public String getUsernameFromToken(String token) throws Exception {
		Claims claims = (Claims) Jwts.parser().setSigningKey(jwtKey.getBytes("UTF-8")).parseClaimsJws(token).getBody();
		return (String) claims.get("username");
	}

	public List<String> getPermissoesFromUsuarioByUsername(String username) {
		List<String> descPermissoes = new ArrayList<>();
		for (Permissao p : usuarioService.findByUsername(username).getPermissoes()) {
			descPermissoes.add(p.getDescricao());
		}
		return descPermissoes;
	}

//	public List<String> getPermissoesFromToken(String token) {
//		return this.getPermissoesFromUsuarioByUsername(this.getUsernameFromToken(token));
//	}
}
