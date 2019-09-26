package com.judson.desafio.api.interceptor;

import java.util.Arrays;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.judson.desafio.api.entity.Permissao;
import com.judson.desafio.api.entity.Usuario;
import com.judson.desafio.exception.UnauthorizedUserException;
import com.judson.desafio.util.SecurityUtil;

public class CustomSecurityInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private SecurityUtil securityUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;

		if (handlerMethod.getBean().getClass().getName().startsWith("org.springframework")) {
			return true;
		}

		if (handlerMethod.getMethod().isAnnotationPresent(PermitAll.class)) {
			return true;
		}

		if (handlerMethod.getMethod().isAnnotationPresent(RolesAllowed.class)) {
			return authorizeRequest(request, handlerMethod);
		}
		return false;
	}

	private boolean authorizeRequest(HttpServletRequest request, HandlerMethod handlerMethod)
			throws UnauthorizedUserException, AuthenticationException {

		String header = request.getHeader("Authorization");
		if (header == null || header.equals("")) {
			throw new AuthenticationException("Missing Authorization header");
		}
		String token = header.replace("Bearer ", "");
		RolesAllowed annotation = handlerMethod.getMethodAnnotation(RolesAllowed.class);
		List<String> rolesAllowed = Arrays.asList(annotation.value());

		Usuario usuarioLogado = securityUtil.getUsuarioFromToken(token);
		if (usuarioLogado != null) {

			for (Permissao p : usuarioLogado.getPermissoes()) {
				if (rolesAllowed.contains(p.getDescricao())) {
					request.getSession().setAttribute(Usuario.USER_ID, usuarioLogado.getId());
					return true;
				}
			}

		}
		
		throw new UnauthorizedUserException("Privilégios insuficientes.");

	}
//	private void authorizeRequest(HttpServletRequest request, HandlerMethod handlerMethod)
//			throws UnauthorizedUserException {
//		try {
//			String token = request.getHeader("Authorization").replace("Bearer ", "");
//			RolesAllowed annotation = handlerMethod.getMethodAnnotation(RolesAllowed.class);
//			List<String> rolesAllowed = Arrays.asList(annotation.value());
//			
//			Usuario usuarioLogado = securityUtil.getUsuarioFromToken(token);
//			if (usuarioLogado != null) {
//				
//				for (Permissao p : usuarioLogado.getPermissoes()) {
//					if (rolesAllowed.contains(p.getDescricao())) {
//						request.getSession().setAttribute("usuarioLogado", usuarioLogado.getUsername());
//						return true;
//					}
//				}
//				
//			} else {
//				return false;
//			}
//			
//		} catch (Exception e) {
//			throw new UnauthorizedUserException("Acesso não autorizado.");
//		}
//	}
}
