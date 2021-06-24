package in.shuvam.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import in.shuvam.jwt.JwtTokenUtil;
import reactor.core.publisher.Mono;
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager{
	
	@Autowired
	private JwtTokenUtil token;
	@SuppressWarnings("unchecked")
	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken=authentication.getCredentials().toString();
		String username;
		try {
			username= token.getUsernameFromToken(authToken);
		}
		catch(Exception e) {
			username=null;
		}
		if(username!=null && !token.isTokenExpired(authToken)) {
			UsernamePasswordAuthenticationToken auth= new UsernamePasswordAuthenticationToken(username,username, null);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return Mono.just(auth);
		}
		else
			return Mono.empty();
	}

}
