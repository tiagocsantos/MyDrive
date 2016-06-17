package pt.ulisboa.tecnico.mydrive.service;


import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.exception.MyDriveException;

public class ChangeDirectoryService extends MyDriveService {
	private String path;
	private long tok;
	private String currentDir;
	
	
	public ChangeDirectoryService(long token,String pth){
		tok = token;
		path = pth;
		currentDir=null;
		
	}
	
	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = getMyDrive();
		Login l1 = md.getLogin(tok);
		if (path.startsWith("/")) {
			l1.setCurrentDirLog(l1.getCurrentDir().processPath(path));
		}
		else{
			l1.setCurrentDirLog(l1.getCurrentDir().processPath(path));
		}
		
		currentDir = l1.getCurrentDir().getPath();
	}
	
	public final String result(){
		return currentDir;
	}

}