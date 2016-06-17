package pt.ulisboa.tecnico.mydrive.exception;

public class InconsistentContentException extends MyDriveException{
	private static final long serialVersionUID = 1L;
	    
    public InconsistentContentException() {
        super("Inconsistent Content in File");
    }
}