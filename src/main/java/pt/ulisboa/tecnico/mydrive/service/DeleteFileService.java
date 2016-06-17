package pt.ulisboa.tecnico.mydrive.service;

import pt.ulisboa.tecnico.mydrive.domain.Directory;
import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;


public class DeleteFileService extends MyDriveService {
	private long _token;
	private String _name;
	
	public DeleteFileService(long token, String name) {
		_name = name;
		_token = token;
	}

	@Override
	protected void dispatch() {
		MyDrive md  = getMyDrive();
		Login log = md.getLogin(_token);
		
		Directory d = log.getCurrentDir();
		d.remove(_name, log.getSessionOwner());
	}
	
	
	
}
