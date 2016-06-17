package pt.ulisboa.tecnico.mydrive.service;

import java.lang.reflect.InvocationTargetException;

import pt.ulisboa.tecnico.mydrive.domain.Files;
import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.exception.MyDriveException;

public class ExecuteFileService extends MyDriveService {
	private long _token;
	private String _path;
	private String _args;
	
	public ExecuteFileService(long token, String path, String args){
		_token = token;
		_path = path;
		_args = args;
	}
	
	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md  = getMyDrive();
		Login log = md.getLogin(_token);
		
		Files file = log.getCurrentDir().processPath(_path);	
		try {
			file.execute(_args.split(","), md.getLogin(_token).getSessionOwner());
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			System.out.println("Could not Execute File");
		}

	}

}
