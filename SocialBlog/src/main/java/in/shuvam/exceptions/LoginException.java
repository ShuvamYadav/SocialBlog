package in.shuvam.exceptions;

@SuppressWarnings("serial")
public class LoginException extends Throwable{

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Username or Password is wrong";
	}

}
