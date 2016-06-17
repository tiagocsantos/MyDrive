package pt.ulisboa.tecnico.mydrive.exception;

public class InvalidPathException extends MyDriveException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public InvalidPathException(){
		super("Path cannot have more than 1024 characters");
	}
}
