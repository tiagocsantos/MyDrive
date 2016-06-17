package pt.ulisboa.tecnico.mydrive.exception;

public class InvalidFileNameException extends MyDriveException{
	private static final long serialVersionUID = 1L;

    public InvalidFileNameException() {
        super("File Name cannot contain: ( /, EOF) and cannot be null.");
    }
}