package pt.ulisboa.tecnico.mydrive.service;

import pt.ulisboa.tecnico.mydrive.domain.App;
import pt.ulisboa.tecnico.mydrive.domain.Directory;
import pt.ulisboa.tecnico.mydrive.domain.FileText;
import pt.ulisboa.tecnico.mydrive.domain.Link;
import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.domain.User;
import pt.ulisboa.tecnico.mydrive.exception.InvalidArgContentException;
import pt.ulisboa.tecnico.mydrive.exception.MyDriveException;

public class CreateFileService extends MyDriveService {
	private long _token;
	private String _name;
	private String _type;
	private String _content =null;
	
	public CreateFileService(long token, String name, String type){
		_name = name;
		_token = token;
		_type = type;
		if (type.equals("l"))
			throw new InvalidArgContentException();
		
	}
	public CreateFileService(long token, String name, String type, String content){
		_token = token;
		_name = name;
		_type = type;
		_content = content;
		if (type.equals("dir") | (type.equals("l") && (content==""))){
			throw new InvalidArgContentException();
		}
	}
	
	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md  = getMyDrive();
		Login log = md.getLogin(_token);
		Directory dir = log.getCurrentDir();
		User u = log.getSessionOwner();
		log.isValid();

		if (_type.equals("dir")){
			new Directory(_name, dir, u, md.getIDF());
		}
		else if (_type.equals("textfile")){
			new FileText(dir, _name, _content, u,  md.getIDF());
			
		}
		else if (_type.equals("link")){
			new Link(dir, _name, _content, u, md.getIDF());
		}
		else if (_type.equals("app")){
			new App(dir, _name, _content, u, md.getIDF());
		}
		else throw new InvalidArgContentException();
	}

}
