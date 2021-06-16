package in.shuvam.repository;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;

import in.shuvam.entity.Posts;
import in.shuvam.entity.Users;
import in.shuvam.exceptions.DefaultException;
import in.shuvam.exceptions.UsernameNotFound;
import in.shuvam.exceptions.UsernameTaken;
import reactor.core.publisher.Mono;

public class CustomRepositoryImpl implements CustomRepository{
	private final ReactiveMongoTemplate template;
	@Autowired
	public CustomRepositoryImpl(ReactiveMongoTemplate template) {
		this.template= template; 
	}
	@Override
	public Mono<Users> changeUsername(String username, String newUsername) {
		Query query=new Query(Criteria.where("username").is(username));
		Update update= new Update().set("username",newUsername);
		return template.findAndModify(query, update,Users.class)
				.onErrorMap(e-> new UsernameTaken());
	}

	@Override
	public Mono<Users> addPosts(String username, Posts post) {
		Query query=new Query(Criteria.where("username").is(username));
		post.setLikes(0);
		post.setLikedby(Collections.emptyList());
		Update update= new Update().addToSet("posts",post);
		return template.findAndModify(query, update,Users.class).switchIfEmpty(Mono.error(new UsernameNotFound()));
	}

	@Override
	public Mono<Users> removePost(String username, String postid) {
		Query query=new Query(Criteria.where("username").is(username));
		Update update= new Update().pull("posts", new BasicDBObject("id",postid));
		return template.findAndModify(query, update,Users.class).switchIfEmpty(Mono.error(new DefaultException()));
	}
	@Override
	public Mono<Users> likepost(String username, String postid) {
		Update update = new Update();
		update.inc("posts.$.likes");
		return template.findAndModify(new Query(Criteria.where("username").is(username))
				.addCriteria(Criteria.where("posts.id").is(postid)),update,Users.class).switchIfEmpty(Mono.error(new DefaultException()));
	}

}
