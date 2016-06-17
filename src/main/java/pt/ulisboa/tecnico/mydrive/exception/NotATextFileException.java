package pt.ulisboa.tecnico.mydrive.exception;

public class NotATextFileException extends MyDriveException{
    private static final long serialVersionUID = 1L;
	
    private String name;
    
    public NotATextFileException(String name1){
    	name = name1;
    }
    
    public String getName(){
    	return name;
    }
    
    @Override
    public String getMessage() {
        return  name + " is not a Text File.";
    }
}