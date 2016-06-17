package pt.ulisboa.tecnico.mydrive.exception;

public class DirectoryNotEmptyException extends MyDriveException{
	private static final long serialVersionUID = 1L;

    public DirectoryNotEmptyException() {
        super("Directory is not empty.");
    }
}