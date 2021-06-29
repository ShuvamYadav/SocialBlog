package in.shuvam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.shuvam.entity.Login;
import in.shuvam.entity.Token;
import in.shuvam.entity.Users;
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
	@PostMapping(value ="/signup")
	public Mono<Users> signUp(@RequestBody Users user){
		Mono<Users> u= Mono.just(user);
		return u.map(e -> {
			e.setPassword(new BCryptPasswordEncoder().encode(e.getPassword()));
			return user;
		}).flatMap(e -> repo.save(e))
				.onErrorMap(err-> new UsernameTaken());
				
	}
	@GetMapping("/login")
	public Mono<Token> login(@RequestBody Login login){
		return repo.findByUsername(login.getUsername())
				.flatMap(user->{
					if(new BCryptPasswordEncoder().matches(login.getPassword(),user.getPassword())) {
						return Mono.just(new Token(util.generateToken(user)));
					}
					else
						return Mono.error(new LoginException());
				}).switchIfEmpty(Mono.error(new UsernameNotFound()));
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
	
	@GetMapping("/showprofile/{username}")
	public Mono<Users> getUser(@PathVariable String username){
		return repo.showUser(username);
	}
	@GetMapping("/profile")
	public Mono<Users> getprofile(Authentication auth){
		return repo.findByUsername(auth.getName());
	}

}
