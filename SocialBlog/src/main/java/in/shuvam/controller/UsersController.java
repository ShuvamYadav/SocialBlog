package in.shuvam.controller;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.shuvam.entity.Users;
import in.shuvam.exceptions.UsernameNotFound;
import in.shuvam.exceptions.UsernameTaken;
import in.shuvam.repository.BlogRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UsersController {
	
	@Autowired
	BlogRepo repo;
	ConcurrentHashMap<String,ServerSentEvent<String>> map=new ConcurrentHashMap<>();
	@PostMapping(value ="/signup")
	public Mono<Users> signUp(@RequestBody Users user){
		return repo.save(user)
				.onErrorMap(e-> new UsernameTaken());
	}
	
	@GetMapping("/all")
	public Flux<Users> getall(){
		return repo.findAll();
	}
	
	@DeleteMapping("/remove/{username}")
	public Mono<String> delete(@PathVariable String username){
		return repo.deleteByUsername(username)
				.switchIfEmpty(Mono.error(new UsernameNotFound()))
				.thenReturn("Removed");
	}
	
	@GetMapping("/{username}")
	public Mono<Users> getUser(@PathVariable String username){
		return repo.findByUsername(username)
				.switchIfEmpty(Mono.error(new UsernameNotFound()));
	}

}
