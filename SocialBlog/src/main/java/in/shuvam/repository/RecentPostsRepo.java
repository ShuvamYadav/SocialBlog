package in.shuvam.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import in.shuvam.entity.RecentPosts;
import reactor.core.publisher.Flux;

public interface RecentPostsRepo extends ReactiveMongoRepository<RecentPosts,String>{
	@Tailable
	Flux<RecentPosts> findRecentPostsBy();
}
