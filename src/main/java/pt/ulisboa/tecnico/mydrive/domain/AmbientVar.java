package pt.ulisboa.tecnico.mydrive.domain;

import pt.ulisboa.tecnico.mydrive.exception.PermissionDeniedException;

public class AmbientVar extends AmbientVar_Base {
    
	public AmbientVar(String name, String value, Login login) {
		super.setName(name);
		super.setValue(value);
		super.setLogin(login);
	}
	
	@Override
	public void setName(String n){
		throw new PermissionDeniedException();
	}
	
	@Override
	public void setLogin(Login n){
		throw new PermissionDeniedException();
	}
	
	
    
}
