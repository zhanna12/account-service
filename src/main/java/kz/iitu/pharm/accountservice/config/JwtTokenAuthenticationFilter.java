package kz.iitu.pharm.accountservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
        String header = request.getHeader("Authorization");
        // 2. validate the header and check the prefix
        if(header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);  		// If not valid, go to the next filter.
            return;
        }
        // 3. Get the token
        String token = header.replace("Bearer ", "");
        // exceptions might be thrown in creating the claims if for example the token is expired
        try {
            // 4. Validate the token
            Claims claims = Jwts.parser()
                    .setSigningKey("secret-key".getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            if(username != null) {
                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) claims.get("authorities");

                System.out.println("authorities = " + authorities);

                // 5. Create auth object
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                // 6. Authenticate the user
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            // In case of failure. Make sure it's clear; so guarantee user won't be authenticated
            SecurityContextHolder.clearContext();
        }
        // go to the next filter in the filter chain
        chain.doFilter(request, response);
    }
}