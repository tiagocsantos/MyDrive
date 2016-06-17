package pt.ulisboa.tecnico.mydrive.exception;

public class UserExistsException extends MyDriveException{
    private static final long serialVersionUID = 1L;
	
    private String SameName;
    
    public UserExistsException(String SameName1){
    	SameName = SameName1;
    }
    
    public String getSameName(){
    	return SameName;
    }
    
    @Override
    public String getMessage() {
        return "User " + SameName + " already exist";
    }
}