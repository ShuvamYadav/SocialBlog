package in.shuvam.exceptions;

@SuppressWarnings("serial")
public class Alreadyliked extends Throwable{

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "You have already liked this post.";
	}

}
