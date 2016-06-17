package pt.ulisboa.tecnico.mydrive.exception;

public class ImportDocException extends MyDriveException{
    private static final long serialVersionUID = 1L;

    public ImportDocException() {
        super("Error based on XML Import");
    }

}