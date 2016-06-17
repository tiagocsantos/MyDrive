package pt.ulisboa.tecnico.mydrive.exception;

public class IncorrectUsernameException extends MyDriveException{
    private static final long serialVersionUID = 1L;

    private String username;

    public IncorrectUsernameException(String username1) {
        username = username1;
    }

    public String getUserName() {
        return username;
    }

    @Override
    public String getMessage() {
        return "Username with a invalid format:" + username;
    }
}