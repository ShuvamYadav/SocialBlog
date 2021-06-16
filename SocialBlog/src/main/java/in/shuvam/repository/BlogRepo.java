package in.shuvam.repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import in.shuvam.entity.Users;
import reactor.core.publisher.Mono;
@Repository
public interface BlogRepo extends ReactiveMongoRepository<Users,String>,CustomRepository{
	@DeleteQuery
	public Mono<Void> deleteByUsername(String username);
	public Mono<Users> findByUsername(String username);
}
