package com.example.jpa.jpa1.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.jpa.jpa1.Security.JwtUtil;
import com.example.jpa.jpa1.Service.CustomUserDetailsService;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		 String path = request.getServletPath();

		    // Skip JWT check for login and signup
		    if (path.startsWith("/api/login") || path.startsWith("/api/signup")) {
		        filterChain.doFilter(request, response);
		        return;
		    }

		
		String authHeader = request.getHeader("Authorization");
		
	
		try {	
			String username = null;
	        String token = null;
	
	        if(authHeader != null && authHeader.startsWith("Bearer ")) {
	        	token = authHeader.substring(7);
	        	username = jwtUtil.extractUsername(token);
	        }
	        
	        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        	
	        	UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	        	
	        	if(jwtUtil.validateToken(token, userDetails.getUsername())) {
	        		
	        		UsernamePasswordAuthenticationToken authToken = 
	        						new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
	        		
	        		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	        		
	        		SecurityContextHolder.getContext().setAuthentication(authToken);
	        	}
	        	
	        }
	        
	        filterChain.doFilter(request, response);
			
		}catch(io.jsonwebtoken.ExpiredJwtException e){
		
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json");
	        response.getWriter().write("{\"error\": \"Access token expired\"}");
	        return;
			
		}catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        return;
		}
	
	
	}
	
		
}
