package pt.ulisboa.tecnico.mydrive.exception;

public class UserDoesNotExistException extends MyDriveException{
    private static final long serialVersionUID = 1L;

    private String username;

    public UserDoesNotExistException(String username1) {
        username = username1;
    }

    public String getUserName() {
        return username;
    }

    @Override
    public String getMessage() {
        return "User " + username + " does not exist";
    }
}