package in.shuvam.repository;

import in.shuvam.entity.Posts;
import in.shuvam.entity.Users;
import reactor.core.publisher.Mono;

public interface CustomRepository {
	
	Mono<Users> changeUsername(String username,String newUsername);
	Mono<Users> addPosts(String username, Posts post);
	Mono<Users> removePost(String username,String postid);
	Mono<Users> likepost(String user,String username,String postid);
}
