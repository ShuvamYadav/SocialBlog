package in.shuvam.entity;

import java.util.List;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor 
@Data
public class Posts {
	@NonNull
	private String id;
	@NonNull
	private String content;
	private int likes;
	private List<String> likedby;	
}
