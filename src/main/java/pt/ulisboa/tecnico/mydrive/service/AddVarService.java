package pt.ulisboa.tecnico.mydrive.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pt.ulisboa.tecnico.mydrive.domain.AmbientVar;
import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.exception.MyDriveException;
import pt.ulisboa.tecnico.mydrive.service.dto.VarDto;

public class AddVarService extends MyDriveService {
	private long token;
	private String name;
	private String value;
	private List<VarDto>list = new ArrayList<VarDto>();
	
	

	public AddVarService(long tok, String name, String value){
		token = tok;
		this.name = name;
		this.value = value;
	}
	
	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = getMyDrive();
		Login l1 = md.getLogin(token);
		l1.addVar(name, value);
		for (AmbientVar v : l1.getVarSet()){
			list.add(new VarDto(v.getName(), v.getValue()));
		}
	}
	
	public List<VarDto> result(){
		return list;
	}

}
