package pt.ulisboa.tecnico.mydrive.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.domain.Directory;
import pt.ulisboa.tecnico.mydrive.domain.Directory_Base;
import pt.ulisboa.tecnico.mydrive.domain.Files;
import pt.ulisboa.tecnico.mydrive.domain.Link;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.exception.MyDriveException;
import pt.ulisboa.tecnico.mydrive.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.mydrive.service.dto.DirectoryDto;

public class ListDirectoryService extends MyDriveService {

	private List<DirectoryDto> list;
	private long _token;
	private String _path;
	
	public ListDirectoryService(long token, String path) {
		list = new ArrayList<DirectoryDto>();
		_token =token;
		_path = path;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = getMyDrive();
		Login login = md.getLogin(_token);
		Files dir = login.getCurrentDir().processPath(_path);
		Directory pai = dir.getDirectory();
		
		if (	(login.getSessionOwner().equals(dir.getOnwer()))
				| (login.getSessionOwner().getUsername().equals("root")) 
				| (dir.getMask().charAt(4) == ('r') && !(login.getSessionOwner().getUsername().equals("nobody") ))){
			for (Files f: ((Directory) dir).getFichsSet()){
				if (f.getType().equals("l"))
					list.add(new DirectoryDto(f.getName(),f.getOnwer().getUsername(),f.getType(),f.getSize(),f.getMask(),f.getIdf(),f.getLastmod(),((Link) f).getContent()));
				else
					list.add(new DirectoryDto(f.getName(),f.getOnwer().getUsername(),f.getType(),f.getSize(),f.getMask(),f.getIdf(),f.getLastmod()));
			}
			
			list.add(new DirectoryDto(".",dir.getOnwer().getUsername(),dir.getType(),dir.getSize(),dir.getMask(),dir.getIdf(),dir.getLastmod()));
			list.add(new DirectoryDto("..",pai.getOnwer().getUsername(),pai.getType(),pai.getSize(),pai.getMask(),pai.getIdf(),pai.getLastmod()));
			Collections.sort(list);
		}
		else{
			throw new PermissionDeniedException();
		}
	}

	public List<DirectoryDto> result() {
		return list;
	}

}
