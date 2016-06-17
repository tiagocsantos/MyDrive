package pt.ulisboa.tecnico.mydrive.exception;

public class IncorrectPasswordException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private String username;
	
    public IncorrectPasswordException(String user){
   
    }
    
    public String getUserName(){
    	return username;
    }
    
    @Override
    public String getMessage() {
        return "Incorrect Password for user " + username;
    }

}
