package in.shuvam.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Document
@Data
@AllArgsConstructor
public class RecentPosts {
	@Id
	private String postid;
	private String user;
	private String content;

}
