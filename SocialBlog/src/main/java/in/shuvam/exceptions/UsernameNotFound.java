package in.shuvam.exceptions;

public class UsernameNotFound extends Throwable{

	@Override
	public String getMessage() {
		return "Username does not exist.";
	}
	

}
