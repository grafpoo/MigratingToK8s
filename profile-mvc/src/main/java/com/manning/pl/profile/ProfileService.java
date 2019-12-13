package com.manning.pl.profile;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProfileService implements UserDetailsService {
	
	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct	
	protected void initialize() {
		save(new Profile("admin", "donttell", "ROLE_ADMIN"));
	}

	@Transactional
	public Profile save(Profile profile) {
		profile.setPassword(passwordEncoder.encode(profile.getPassword()));
		profileRepository.save(profile);
		return profile;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Profile profile = profileRepository.findOneByEmail(username);
		if(profile == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return createUser(profile);
	}
	
	public void signin(Profile profile) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(profile));
	}
	
	private Authentication authenticate(Profile profile) {
		return new UsernamePasswordAuthenticationToken(createUser(profile), null, Collections.singleton(createAuthority(profile)));
	}
	
	private User createUser(Profile profile) {
		return new User(profile.getEmail(), profile.getPassword(), Collections.singleton(createAuthority(profile)));
	}

	private GrantedAuthority createAuthority(Profile profile) {
		return new SimpleGrantedAuthority(profile.getRole());
	}

}
