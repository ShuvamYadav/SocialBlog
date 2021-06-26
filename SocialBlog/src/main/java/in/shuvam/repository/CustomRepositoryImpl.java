package in.shuvam.repository;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;

import in.shuvam.entity.Posts;
import in.shuvam.entity.Users;
import in.shuvam.exceptions.Alreadyliked;
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
		return template.exists(new Query(Criteria.where("username").is(newUsername)),Users.class)
				.flatMap(e->{
					if(e) {
						return Mono.error(new UsernameTaken());
					}
					else {
						Query query=new Query(Criteria.where("username").is(username));
						Update update= new Update().set("username",newUsername);
						return template.findAndModify(query, update,Users.class);
					}
				});
	}

	@Override
	public Mono<Users> addPosts(String username, Posts post) {
		Query query=new Query(Criteria.where("username").is(username));
		post.setLikes(0);
		post.setLikedby(Collections.emptyList());
		Update update= new Update().addToSet("posts",post);
		return template.findAndModify(query, update,FindAndModifyOptions.options().returnNew(true),Users.class).switchIfEmpty(Mono.error(new UsernameNotFound()));
	}

	@Override
	public Mono<Users> removePost(String username, String postid) {
		Query query=new Query(Criteria.where("username").is(username));
		Update update= new Update().pull("posts", new BasicDBObject("id",postid));
		return template.findAndModify(query, update,FindAndModifyOptions.options().returnNew(true),Users.class).switchIfEmpty(Mono.error(new DefaultException()));
	}
	@Override
	public Mono<Users> likepost(String user,String username, String postid) {
		Update update = new Update();
		return template.exists(new Query(Criteria.where("username").is(username)).addCriteria(Criteria.where("posts.id").is(postid)).addCriteria(Criteria.where("posts.likedby").in(user)),Users.class)
		.flatMap(e-> {if(e){return Mono.error(new Alreadyliked());}
		else{update.addToSet("posts.$.likedby",user);
		update.inc("posts.$.likes");
		return template.findAndModify(new Query(Criteria.where("username").is(username))
				.addCriteria(Criteria.where("posts.id").is(postid)),update,FindAndModifyOptions.options().returnNew(true),Users.class).switchIfEmpty(Mono.error(new DefaultException()));}});
	}
	@Override
	public Mono<Users> showUser(String username) {
		Query query=new Query(Criteria.where("username").is(username));
		query.fields().exclude("enabled","password");
		return template.findOne(query,Users.class).switchIfEmpty(Mono.error(new UsernameNotFound()));
	}
	

}
