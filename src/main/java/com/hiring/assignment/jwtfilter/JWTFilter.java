package com.hiring.assignment.jwtfilter;

import com.hiring.assignment.oauth2.service.UserEntity;
import com.hiring.assignment.singleton.SingletonLoggedInUsers;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Optional;

@Component
@Order(1)
public class JWTFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			String jwt = this.resolveToken(httpServletRequest);
			if (StringUtils.hasText(jwt)) {

				if (SingletonLoggedInUsers.getInstance().getUserCredentials() != null) {
					Optional<Entry<String, UserEntity>> findFirst = SingletonLoggedInUsers.getInstance()
							.getUserCredentials().entrySet().stream()
							.filter(obj -> obj.getValue().getAccessToken() != null
									&& obj.getValue().getAccessToken().contentEquals(jwt))
							.findFirst();
					if (findFirst.isPresent()) {
						filterChain.doFilter(servletRequest, servletResponse);

					} else {
						throw new Exception();
					}
				} else {
					throw new Exception();
				}

			} else {
				filterChain.doFilter(servletRequest, servletResponse);
			}

		} catch (Exception eje) {

			((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Not Valid JWT Token");

		}

	}

	private String resolveToken(HttpServletRequest request) {

		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			String jwt = bearerToken.substring(7, bearerToken.length());
			return jwt;
		}
		return null;
	}
}
