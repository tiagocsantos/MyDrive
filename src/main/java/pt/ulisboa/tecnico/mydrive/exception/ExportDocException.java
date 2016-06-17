package pt.ulisboa.tecnico.mydrive.exception;

public class ExportDocException extends MyDriveException{
    private static final long serialVersionUID = 1L;

    public ExportDocException() {
        super("Error based on XML Export");
    }

}