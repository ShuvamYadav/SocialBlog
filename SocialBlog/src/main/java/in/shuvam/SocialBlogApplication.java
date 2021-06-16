package in.shuvam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocialBlogApplication{
	
	@Autowired
	public static void main(String[] args) {
		SpringApplication.run(SocialBlogApplication.class, args);
	}

}
