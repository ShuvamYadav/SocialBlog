package in.shuvam.exceptions;

@SuppressWarnings("serial")
public class UsernameTaken extends Throwable{

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Username already taken.";
	}

}
