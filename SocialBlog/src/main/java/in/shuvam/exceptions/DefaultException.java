package in.shuvam.exceptions;

public class DefaultException extends Throwable{

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Username or Post_id not found.";
	}

}
