package in.shuvam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.shuvam.entity.Posts;
import in.shuvam.entity.RecentPosts;
import in.shuvam.entity.Users;
import in.shuvam.exceptions.UsernameNotFound;
import in.shuvam.repository.BlogRepo;
import in.shuvam.repository.RecentPostsRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController 
public class PostsController {
	@Autowired
	BlogRepo repo;
	@Autowired
	RecentPostsRepo RecentRepo;
	@PostMapping("/{username}/post")
	public Mono<Users> post(@PathVariable String username,@RequestBody Posts post){
		RecentRepo.insert(new RecentPosts(post.getId(),username,post.getContent())).subscribe();
			return repo.addPosts(username, post);
	}
	@DeleteMapping("/{username}/posts/{postid}")
	public Mono<Users> deletePost(@PathVariable String username,@PathVariable String postid){
		return repo.removePost(username, postid);
	}
	@PutMapping("/{username}/change/{newusername}")
	public Mono<Users> changeUsername(@PathVariable String username, @PathVariable String newusername){
		return repo.changeUsername(username, newusername);
	}
	@PutMapping("{username}/posts/like/{id}")
	public Mono<Users> likepost(@PathVariable String username,@PathVariable String id){
		return repo.likepost(username, id);
	}
	@GetMapping(value = "/recent", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<RecentPosts> find(){
		return RecentRepo.findRecentPostsBy();
	}
}
