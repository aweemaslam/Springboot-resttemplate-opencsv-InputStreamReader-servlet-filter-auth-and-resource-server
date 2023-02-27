package com.hiring.assignment.oauth2.service;

import com.hiring.assignment.singleton.SingletonLoggedInUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity entity = SingletonLoggedInUsers.getInstance().getUserCredentials().get(username);
		if (entity == null) {
			entity = new UserEntity();
			entity.setUsername(username);
			entity.setPassword(passwordEncoder.encode(username));
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			grantedAuthorities.add(grantedAuthority);
			entity.setGrantedAuthoritiesList(grantedAuthorities);
			SingletonLoggedInUsers.getInstance().getUserCredentials().put(username, entity);
		}

		CustomUser customUser = new CustomUser(entity);
		return customUser;
	}

}
