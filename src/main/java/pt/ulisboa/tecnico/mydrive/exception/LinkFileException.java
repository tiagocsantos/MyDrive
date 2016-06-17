package pt.ulisboa.tecnico.mydrive.exception;

public class LinkFileException extends MyDriveException{
	private static final long serialVersionUID = 1L;
	    
    public LinkFileException() {
        super("Can't write in a Link file");
    }
}