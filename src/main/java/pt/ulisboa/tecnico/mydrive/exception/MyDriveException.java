package pt.ulisboa.tecnico.mydrive.exception;

public class MyDriveException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MyDriveException() {
    }

    public MyDriveException(String msg) {
        super(msg);
    }
}
