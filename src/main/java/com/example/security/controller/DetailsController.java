package com.example.security.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.dto.AuthRequest;
import com.example.security.dto.Product;
import com.example.security.entity.UserInfo;
import com.example.security.service.JwtService;
import com.example.security.service.ProductService;
import com.example.security.service.UserInfoService;

@RestController
public class DetailsController {

	Logger log = LoggerFactory.getLogger(DetailsController.class);
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private UserInfoService userService;
	
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@PostMapping("/adduser")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String addUser(@RequestBody UserInfo user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return userService.addUserDetails(user);
	}
	
	@GetMapping("/welcome")
	public String getWelcomeMsg() {
		log.info("getWelcomeMsg method got executed....");
		return "Welcome To My Application";
	}
	
	@GetMapping("/get/getAllProduct")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<Product>> GetAllProduct(){
		
		return new ResponseEntity<List<Product>>(service.getAllProducts(), null, 200 );
	}
	
	@GetMapping("/get/getProduct")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Product> GetProduct(@RequestParam int id){
		
		return new ResponseEntity<Product>(service.getProductById(id), null, 200 );
	}
	
	
	@PostMapping("/auth")
	public String generateTokrn(@RequestBody AuthRequest authRequest) {
		Authentication  auth= authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if(auth.isAuthenticated()) {
			return jwtService.generateTokenToUse(authRequest.getUsername());
		}else {
			
			throw new UsernameNotFoundException("User Name Not Found with name:"+authRequest.getUsername());
		}
		
		
	}
	
}
