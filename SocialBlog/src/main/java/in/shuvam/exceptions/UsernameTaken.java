package in.shuvam.exceptions;

public class UsernameTaken extends Throwable{

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Username already taken.";
	}

}
