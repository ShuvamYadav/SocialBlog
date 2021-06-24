package in.shuvam.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@Component
@Slf4j
public class SecurityContextRepository implements ServerSecurityContextRepository{
	
	private static final String Prefix="Bearer ";
	@Autowired
	private AuthenticationManager manager;
	@Override
	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
		throw new UnsupportedOperationException("Not Supported Yet");
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange exchange) {
		ServerHttpRequest request= exchange.getRequest();
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		String authToken= null;
		if(authHeader!=null && authHeader.startsWith(Prefix)) {
			authToken= authHeader.replace(Prefix,"");
		}
		else
			log.warn("Ignoring header");
		if(authToken!=null) {
			Authentication auth= new UsernamePasswordAuthenticationToken(authToken, authToken);
			return this.manager.authenticate(auth).map(e-> new SecurityContextImpl(e));
		}
		else
			return Mono.empty();
	}
	

}
