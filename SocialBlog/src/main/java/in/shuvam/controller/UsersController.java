package in.shuvam.controller;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.shuvam.entity.Token;
import in.shuvam.entity.Users;
import in.shuvam.exceptions.DefaultException;
import in.shuvam.exceptions.LoginException;
import in.shuvam.exceptions.UsernameNotFound;
import in.shuvam.exceptions.UsernameTaken;
import in.shuvam.jwt.JwtTokenUtil;
import in.shuvam.repository.BlogRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UsersController {
	
	@Autowired
	BlogRepo repo;
	@Autowired
	JwtTokenUtil util;
	ConcurrentHashMap<String,ServerSentEvent<String>> map=new ConcurrentHashMap<>();
	@PostMapping(value ="/signup")
	public Mono<Users> signUp(@RequestBody Users user){
		Mono<Users> u= Mono.just(user);
		return u.map(e -> {
			e.setPassword(new BCryptPasswordEncoder().encode(e.getPassword()));
			return user;
		}).flatMap(e -> repo.save(e))
				.onErrorMap(err-> new UsernameTaken());
				
	}
	@GetMapping("/login/{username}/{password}")
	public Mono<Token> login(@PathVariable String username,@PathVariable String password){
		return repo.findByUsername(username)
				.flatMap(user->{
					if(new BCryptPasswordEncoder().matches(password,user.getPassword())) {
						return Mono.just(new Token(util.generateToken(user)));
					}
					else
						return Mono.error(new LoginException());
				});
	}
	
	@GetMapping("/all")
	public Flux<Users> getall(){
		return repo.findAll();
	}
	@DeleteMapping("/delete")
	public Mono<String> delete(Authentication auth){
		return repo.deleteByUsername(auth.getName())
				.thenReturn("Deleted");
	}
	
	@GetMapping("/getuser/{username}")
	public Mono<Users> getUser(@PathVariable String username){
		return repo.findByUsername(username)
				.switchIfEmpty(Mono.error(new UsernameNotFound()));
	}

}
