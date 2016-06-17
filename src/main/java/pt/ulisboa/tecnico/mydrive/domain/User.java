package pt.ulisboa.tecnico.mydrive.domain;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

import pt.ulisboa.tecnico.mydrive.exception.IncorrectUsernameException;
import pt.ulisboa.tecnico.mydrive.exception.UserDoesNotExistException;
import pt.ulisboa.tecnico.mydrive.exception.UserExistsException;

import java.util.Iterator;

import org.jdom2.Element;

import pt.ulisboa.tecnico.mydrive.exception.FileDoesNotExistException;
import pt.ulisboa.tecnico.mydrive.exception.IncorrectPasswordException;

public class User extends User_Base {
    
	public User(MyDrive md, String username){
		if(CheckString(username)){
			setUsername(username);
			setMyDrive(md);
		}
	}

	public User(MyDrive md , String username, String name, String password) throws UserExistsException{
		
		try {
			md.getUserByUserName(username);
			throw new UserExistsException(username);
			
		} catch(UserDoesNotExistException e){
			if (CheckString(username)){
				setUsername(username);
				setName(name);
				setMyDrive(md);
				setUmask("rwxd----");
				Directory home = (Directory) md.getRaiz().getInsideDir("home");
				Directory maindir = new Directory(home, this, md.getIDF());
				setMainDir(maindir);
				if (CheckPassword(password,username)){
					if (password == null)
						setPassword(username);
					else 
						setPassword(password);
				}
			}
		}
		
		
	}

	public User() {
		super();
	}
	

	@Override
	public void setMyDrive(MyDrive md){
		if (md == null){
			super.setMyDrive(null);
		}
		else
			md.addUser(this);
	}
	
	public void xmlImport(Element node) {
		String name = node.getChildText("name");
		if (name!=null)
			setName(name);
		else 
			setName(getUsername());
		
		String mask = node.getChildText("umask");
		if (mask==null)
			setUmask("rwxd----");
		else
			setUmask(mask);
		
		String maindir = node.getChildText("maindir");
		Directory home;
		if (maindir!=null){
			home = getMyDrive().getRaiz().processPathCreate(maindir+"/"+getUsername(), this, getMyDrive());
		}
		else {
			home = getMyDrive().getRaiz().processPathCreate("/home/"+getUsername(), this, getMyDrive());
		}
		setMainDir(home);
		
		String pass = node.getChildText("password");
		CheckPassword(pass, getUsername());
		if (pass!=null)
			setPassword(pass);
		else{
			setPassword(getUsername());
		}
	}
    
	public Files getFile(String name) throws FileDoesNotExistException{
		Iterator<Files> ite = getOwnedSet().iterator();
		Files file;
		while(ite.hasNext()){
			file = ite.next();
			if (file.getName().equals(name)){
				return file;
			}
		}
		throw new FileDoesNotExistException(name);
	}
	
	public Boolean CheckString(String toBeChecked) throws IncorrectUsernameException{
		Pattern specialChars = Pattern.compile("[^a-z0-9]" , Pattern.CASE_INSENSITIVE);
		Matcher stringToCheck = specialChars.matcher(toBeChecked);
		if (toBeChecked.equals("") | toBeChecked.length() < 3)
			throw new IncorrectUsernameException(toBeChecked);
		else if (stringToCheck.find() == true)
	        throw new IncorrectUsernameException(toBeChecked);
		else
			return true;
	}
	
	public Boolean CheckPassword(String password,String username) throws IncorrectPasswordException{
		if (password == null && username.length()<8)
			throw new IncorrectPasswordException(username);
		else if (password!=null && password.length() < 8) 
			throw new IncorrectPasswordException(username);
		else
			return true;
	}
	
	public Element exportXml() {
		Element element = new Element("user");
		
		element.setAttribute("username", getUsername());
		
		Element nameElement = new Element("name");
		nameElement.setText(getName());
		element.addContent(nameElement);
				
		Element passwordElement = new Element("password");
		passwordElement.setText(getPassword());
		element.addContent(passwordElement);
		
		Element umaskElement = new Element("umask");
		umaskElement.addContent(getUmask());
		element.addContent(umaskElement);
		
		Element mainDirElement = new Element("maindir");
		mainDirElement.addContent(getMainDir().getPath());
		element.addContent(mainDirElement);
		
		return element;
	}
	public void remove() {
		
		getMainDir().remove();
		for (Files f : getOwnedSet()){
			f.remove();
		}
		setMyDrive(null);
		deleteDomainObject();
		
	}
	public boolean log(String pass)throws IncorrectPasswordException {
		if (getPassword().equals(pass))
			return true;
		else throw new IncorrectPasswordException(getUsername());
	}
	
	public FileText getAssociation(String name) {
		//to be mocked
		return null;
	}

}
