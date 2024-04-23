package com.example.security.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String key="255c632e9cfe78dc6d97a20cac49366f453849d9b3b8ffbac45bbd1e64dae380";
	
	
	
	public String extractUserName(String token) {
		
		return extractClaim(token, Claims::getSubject);
	}
	
	
	
	private <T> T extractClaim(String token, Function<Claims, T>claimsResolver) {
		final Claims claims= extractAllClaims(token);
		return claimsResolver.apply(claims);
	}


	private Claims extractAllClaims(String token) {
		// TODO Auto-generated method stub
		return Jwts
				.parserBuilder()
				.setSigningKey(getSingkey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}


	public String generateTokenToUse(String userName) {
		
		Map<String, Object> claims= new HashMap<String, Object>();
		
		return createToken(claims, userName);
	}

	private String createToken(Map<String, Object> claims, String userName) {
		
		return Jwts.builder().setClaims(claims)
				.setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*5))
				.signWith(getSingkey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getSingkey() {
		byte[] keyBytes= Decoders.BASE64.decode(key);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		
		final String userName= extractUserName(token);
		
		return (userName.equals(userDetails.getUsername()) && !isTokenExpaired(token));
	}


	private boolean isTokenExpaired(String token) {
		return extractExpiration(token).before(new Date());
	}



	private Date extractExpiration(String token) {
		
		return extractClaim(token, Claims::getExpiration);
	}

}
