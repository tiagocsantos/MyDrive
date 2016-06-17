package pt.ulisboa.tecnico.mydrive.exception;

public class PermissionDeniedException extends MyDriveException{
    private static final long serialVersionUID = 1L;

    public PermissionDeniedException() {
        super("User without permission");
    }

}