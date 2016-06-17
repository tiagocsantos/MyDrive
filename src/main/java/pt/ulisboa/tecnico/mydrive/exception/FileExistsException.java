package pt.ulisboa.tecnico.mydrive.exception;

public class FileExistsException extends MyDriveException{
    private static final long serialVersionUID = 1L;
	
    private String SameName;
    
    public FileExistsException(String SameName1){
    	SameName = SameName1;
    }
    
    public String getSameName(){
    	return SameName;
    }
    
    @Override
    public String getMessage() {
        return "File " + SameName + " already exist";
    }
}