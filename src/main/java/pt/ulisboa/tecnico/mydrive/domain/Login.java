package pt.ulisboa.tecnico.mydrive.domain;

import java.math.BigInteger;
import java.util.Random;
import java.util.Set;

import org.joda.time.DateTime;

import pt.ulisboa.tecnico.mydrive.exception.IncorrectPasswordException;
import pt.ulisboa.tecnico.mydrive.exception.InvalidSessionException;
import pt.ulisboa.tecnico.mydrive.exception.NotADirectoryException;
import pt.ulisboa.tecnico.mydrive.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.mydrive.exception.UserDoesNotExistException;


public class Login extends Login_Base {
    
    public Login(String username, String pass, MyDrive md, boolean expired) throws UserDoesNotExistException, IncorrectPasswordException {
    	md.cleanInvalidTokens();
    	long token;
    	if (pass.length()<8 && !username.equals("root") && !username.equals("nobody"))
    		throw new IncorrectPasswordException(pass);
    	do {
    		token = new BigInteger(64, new Random()).longValue();
    	}
    	while (!md.checkToken(token));
    	
    	User user = md.getUserByUserName(username);
  
    	if (user.log(pass)){
    		super.setMyDrive(md);
    		super.setToken(token);
    		super.setSessionOwner(user);
    		setCurrentDir(user.getMainDir());
    		DateTime date = new DateTime();
    		if (user.getUsername().equals("root")){
    			date.plusHours(1);
    			date.plusMinutes(50);
    		}
    		if (expired){
    			date =date.minusHours(3);
    		}
    		super.setLogintime(date);
    	}   	
    }

	public boolean compareToken(long token) {
		return token == getToken();
	}

	public void isValid() throws InvalidSessionException {
		if (super.getSessionOwner().getUsername().equals("nobody"))
			return;
		DateTime now = new DateTime();
		DateTime loginTime = getLogintime();
		if (now.isAfter(loginTime.plusHours(2)))
			throw new InvalidSessionException();
	}

	public void setCurrentDirLog(Files directory) throws NotADirectoryException {
		if (directory instanceof Directory){
			setCurrentDir((Directory) directory);
		}
		else{
			throw new NotADirectoryException(directory.getName());
		}
	}
	
	@Override
	public void setLogintime(DateTime x){
		throw new PermissionDeniedException();
	}
    
	@Override
	public void setToken(long x){
		throw new PermissionDeniedException();
	}
	
	@Override 
	public void setMyDrive(MyDrive md){
		throw new PermissionDeniedException();
	}
	
	@Override
	public void setSessionOwner(User u){
		throw new PermissionDeniedException();
	}
	
	public void remove(){
		super.setMyDrive(null);
		super.setSessionOwner(null);
		setCurrentDir(null);
		super.setLogintime(null);
		deleteDomainObject();
	}

	public void addVar(String name, String value) {
		boolean contains = false;
		if (name=="")
			return;
		for (AmbientVar v : super.getVarSet()){
			if (v.getName().equals(name)){
				v.setValue(value);
				contains = true;
			}
		}
		if (!contains){
			new AmbientVar(name, value, this);
		}
	}
	
}
