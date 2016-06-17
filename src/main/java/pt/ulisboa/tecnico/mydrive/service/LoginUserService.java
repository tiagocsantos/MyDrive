package pt.ulisboa.tecnico.mydrive.service;

import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.exception.MyDriveException;

public class LoginUserService extends MyDriveService {
	private String username;
	private String password;
	private boolean expired = false;
	long token;
	
	public LoginUserService(String user , String pass){
		username = user;
		password = pass;
	}
	
	public LoginUserService(String user, String pass, boolean expired ){
		username = user;
		password = pass;
		expired = true;
	}
	
	@Override
	protected void dispatch() throws MyDriveException {
		Login sessao = new Login (username, password, getMyDrive(), expired);
		token = sessao.getToken();

	}
	
	public final long result(){
		return token;
	}

}
