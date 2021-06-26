package in.shuvam.exceptions;

@SuppressWarnings("serial")
public class UsernameNotFound extends Throwable{

	@Override
	public String getMessage() {
		return "Username does not exist.";
	}
	

}
