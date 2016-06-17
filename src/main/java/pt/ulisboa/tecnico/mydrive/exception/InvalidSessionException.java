package pt.ulisboa.tecnico.mydrive.exception;

public class InvalidSessionException extends MyDriveException {
	
	private static final long serialVersionUID = 1L;

	@Override
    public String getMessage() {
        return "Your session has expired.";
    }
}