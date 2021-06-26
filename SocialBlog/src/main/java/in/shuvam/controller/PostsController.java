package in.shuvam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
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
import in.shuvam.exceptions.IdTaken;
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
	@Autowired
	ReactiveMongoTemplate template;
	@PostMapping("/newpost")
	public Mono<Users> post(@RequestBody Posts post,Authentication auth){
		return template.exists(new Query(Criteria.where("username").is(auth.getName())).addCriteria(Criteria.where("posts.id").in(post.getId())), Users.class)
				.flatMap(e->{
					if(e) {
						return Mono.error(new IdTaken());
					}
					else {
						RecentRepo.insert(new RecentPosts(post.getId(),auth.getName(),post.getContent())).subscribe();
						return repo.addPosts(auth.getName(), post);
					}
				});
		
	}
	@DeleteMapping("/posts/{postid}")
	public Mono<Users> deletePost(Authentication auth,@PathVariable String postid){
		return repo.removePost(auth.getName(), postid);
	}
	@PutMapping("/changeusername/{newusername}")
	public Mono<Users> changeUsername(Authentication auth,@PathVariable String newusername){
		return repo.changeUsername(auth.getName(), newusername);
	}
	@PutMapping("{username}/posts/like/{id}")
	public Mono<Users> likepost(@PathVariable String username,@PathVariable String id,Authentication auth){
		return repo.likepost(auth.getName(),username, id);
	}
	@GetMapping(value = "/recent", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<RecentPosts> find(){
		return RecentRepo.findRecentPostsBy();
	}
}
