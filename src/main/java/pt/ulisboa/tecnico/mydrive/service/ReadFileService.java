package pt.ulisboa.tecnico.mydrive.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.ulisboa.tecnico.mydrive.domain.*;
import pt.ulisboa.tecnico.mydrive.exception.*;

public class ReadFileService extends MyDriveService{
	long _token;
	private String fileName;
	private String content;

	public ReadFileService(long token, String string) {
		_token=token;
		fileName=string;
	}

	@Override
	protected void dispatch() throws MyDriveException, InvalidSessionException, FileDoesNotExistException, PermissionDeniedException, NotATextFileException {
		MyDrive md=getMyDrive();
		Login user= md.getLogin(_token);
		Directory currentDir=user.getCurrentDir();
		Files file=currentDir.getInsideDir(fileName);
		
		if ((file.getOnwer().equals(user.getSessionOwner() )) | (user.getSessionOwner().getUsername().equals("root")) 
			| (user.getSessionOwner().getUmask().charAt(4) == ('r') && !(user.getSessionOwner().getUsername().equals("nobody") )))
			content=file.readFile();
		else 
			throw new PermissionDeniedException();
	}
	 public final String result() {
	        return content;
	    }
	
}