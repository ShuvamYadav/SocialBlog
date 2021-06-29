package in.shuvam.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	
	@Autowired
	private AuthenticationManager manager;
	@Autowired
	private SecurityContextRepository ContextRepo;
	
	@Bean
	SecurityWebFilterChain chain(ServerHttpSecurity http) {
		return http.cors().disable()
				.exceptionHandling()
				.authenticationEntryPoint((swe,e)-> Mono.fromRunnable(()->{
					swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				})).accessDeniedHandler((swe,e)-> Mono.fromRunnable(()->{
					swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
				})).and()
				.csrf().disable()
				.authorizeExchange()
				.pathMatchers("/signup","/login").permitAll()
				.anyExchange().authenticated()
				.and()
				.authenticationManager(manager)
				.securityContextRepository(ContextRepo)
				.build();
	}
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

}
