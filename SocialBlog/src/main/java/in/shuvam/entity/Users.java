package in.shuvam.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@NoArgsConstructor
@AllArgsConstructor 
@Data 
@Validated
public class Users{	
	@Id
	private String id;
	@Indexed(unique = true)
	@NonNull
	private String username;
	private String password;
	private Boolean enabled=true;
	private List<Posts> posts;
}