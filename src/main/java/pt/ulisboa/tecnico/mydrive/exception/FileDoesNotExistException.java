package pt.ulisboa.tecnico.mydrive.exception;

public class FileDoesNotExistException extends MyDriveException{
    private static final long serialVersionUID = 1L;

    private String name;

    public FileDoesNotExistException(String name1) {
        name = name1;
    }

    public String getUserName() {
        return name;
    }

    @Override
    public String getMessage() {
        return "File " + name + " does not exist";
    }
}