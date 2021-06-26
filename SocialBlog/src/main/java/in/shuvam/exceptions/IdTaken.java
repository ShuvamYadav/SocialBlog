package in.shuvam.exceptions;

@SuppressWarnings("serial")
public class IdTaken extends Throwable{

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "You have a post with the specified id";
	}

}
