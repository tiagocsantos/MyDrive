package pt.ulisboa.tecnico.mydrive.exception;

public class InvalidArgContentException extends MyDriveException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidArgContentException(){
		super("Creating a Directory cannot provide Content");
	}
}
