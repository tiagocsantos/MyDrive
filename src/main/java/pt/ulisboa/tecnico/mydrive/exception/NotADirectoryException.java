package pt.ulisboa.tecnico.mydrive.exception;

public class NotADirectoryException extends MyDriveException{
    private static final long serialVersionUID = 1L;
	
    private String name;
    
    public NotADirectoryException(String name1){
    	name = name1;
    }
    
    public String getName(){
    	return name;
    }
    
    @Override
    public String getMessage() {
        return  name + " is not a Directory.";
    }
}