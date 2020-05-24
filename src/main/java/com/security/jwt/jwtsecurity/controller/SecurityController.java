package com.security.jwt.jwtsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.jwt.jwtsecurity.model.AuthenticationRequest;
import com.security.jwt.jwtsecurity.model.AuthenticationResponse;
import com.security.jwt.jwtsecurity.service.MyUserDetailsService;
import com.security.jwt.jwtsecurity.util.JwtUtil;

@RestController
@RequestMapping("/security")
public class SecurityController {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	MyUserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello World...";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch(BadCredentialsException e) {
			throw new Exception("Invaild username or password ", e);
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
