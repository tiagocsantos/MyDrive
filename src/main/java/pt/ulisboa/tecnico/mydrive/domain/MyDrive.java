package pt.ulisboa.tecnico.mydrive.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.mydrive.exception.*;

public class MyDrive extends MyDrive_Base {
	static final Logger log = LogManager.getRootLogger();
	   
    private MyDrive() {
    	 setRoot(FenixFramework.getDomainRoot());
    	 setSuperuser(new Root(this));
    	 setVisitor(new Nobody(this));
    	 setIdentificador(3);    	 
    }
    
    public void cleanup() {
        for (User p: getUserSet())
        	p.remove();
    }
    
    
	public static MyDrive getInstance() {
		MyDrive md = FenixFramework.getDomainRoot().getMydrive();
	    if (md != null){
	    	return md;
	    }
	    
		log.trace("new MyDrive");
	        return new MyDrive();
	}
	
	public void readContent(String path) throws InvalidPathException{
		if (!checkPath(path))
			throw new InvalidPathException();
		String content="";
		Files file= getRaiz().processPath(path);
		if(file instanceof FileText){
			content =((FileText) file).getContent();
			System.out.println(content);
		}
		
		else throw new NotATextFileException(file.getName());
	}

	private boolean checkPath(String path) {
		return (path.length()<1024);
	}

	public void xmlImport(Element rootElement) {
		int idf=0;
		String name = null; //nome do elemento a criar
		String username = null; // nome do user dono do elemento
		User user = null; //user dono do elemento
		
			for (Element node: rootElement.getChildren("user")) {
				username = node.getAttributeValue("username");
				try{
					getUserByUserName(username);
					throw new UserExistsException(username);
				}
				catch (UserDoesNotExistException e){
					user = new User(this, username);
					user.xmlImport(node);
				}	
			}
	
			for (Element node: rootElement.getChildren("directory")){
				name = node.getChildText("name");
				username = node.getChildText("owner");
				user = getUserByUserName(username);
				Directory dir = new Directory(name);
				idf = dir.xmlImport(node, user, getRaiz());
				if (idf>getIdentificador())
					setIdentificador(idf);
			}
			
			for (Element node: rootElement.getChildren("link")){
				name = node.getChildText("name");
				username = node.getChildText("owner");
				user = getUserByUserName(username);
				Link newlink = new Link(name);
				idf = newlink.xmlImport(node, user, getRaiz());
				if (idf>getIdentificador())
					setIdentificador(idf);
			}
			
			for (Element node: rootElement.getChildren("textfile")){
				name = node.getChildText("name");
				username = node.getChildText("owner");
				user = getUserByUserName(username);
				FileText newfile = new FileText(name);
				idf = newfile.xmlImport(node, user, getRaiz());	
				if (idf>getIdentificador())
					setIdentificador(idf);
			}
			
			for (Element node: rootElement.getChildren("app")){
				name = node.getChildText("name");
				username = node.getChildText("owner");
				user = getUserByUserName(username);
				App newapp = new App(name);
				idf = newapp.xmlImport(node, user, getRaiz());
				if (idf>getIdentificador())
					setIdentificador(idf);
			}
	
	}
	
	public Document exportxml(){
		Element element = new Element("mydrive");
		Document doc = new Document(element);
		
		Set<User> users = getUserSet();
		
		TreeSet<String> users2 = new TreeSet<String>();
		
		for (User u: users){
			users2.add(u.getName());
		}
		
		for (String name : users2){
			for (User u: users){
				if (u.getName().equals(name) && !u.getName().equals("Super User")){
					element.addContent(u.exportXml());
				}
			}
		}
		
		int i=0;
		ArrayList<Files> files = getRaiz().getInsideFiles();
		Files file = null;
		
		while (!files.isEmpty()){
			file = files.get(i);
			if (file instanceof Directory){
				files.addAll(((Directory)file).getInsideFiles());
			}
			
			if (!( file.isHomeDir() | (file.getName().equals("home") && (file.getDirectory().getName().equals("/"))) )) 
				element.addContent(file.xmlExport());
			
			files.remove(i);
			
		}
		
		
        return doc;
	}
	public User getUserByUserName(String username) throws UserDoesNotExistException {
		if (username.equals("root"))
			return getSuperuser();
		
		if (username.equals("nobody"))
			return getVisitor();
		
		Set<User> users = getUserSet();
		Iterator<User> lista = users.iterator();
		User user;
		while (lista.hasNext()){
			user = lista.next();
			if (user.getUsername().equals(username)){
				return user;
			}
		}
		throw new UserDoesNotExistException(username);
	}

	//Listagem simples de uma diretoria : dado um caminho devolve uma lista com os nomes dos ficheiros existentes na diretoria
	public void ListDirectory(String path) throws InvalidPathException{
		if (!checkPath(path))
			throw new InvalidPathException();
		Files file = getRaiz().processPath(path);
		if (file instanceof Directory)
			((Directory) file).ListDir();
		else 
			throw new NotADirectoryException(file.getName());
		
	}
		
	public int getIDF(){
		int idf = getIdentificador();
		setIdentificador(idf+1);
		return idf;
	}

	public void createFile(String path, String name, String content) throws InvalidPathException{
			if (!checkPath(path))
				throw new InvalidPathException();
		Directory dir = (Directory)getRaiz().processPath(path);
		if (dir.checkExistence(name))
			throw new FileExistsException(name);
		new FileText(dir, name, content, getSuperuser(), getIDF());
	}
	
	public void createDirectory(String path) throws InvalidPathException{
			if (!checkPath(path))
				throw new InvalidPathException();
		getRaiz().processPathCreate(path, getSuperuser(), this);
	}

	public void deleteFile(String path)	throws InvalidPathException{
		if (!checkPath(path))
			throw new InvalidPathException();
		Files file = getRaiz().processPath(path);
		file.remove();

	}

	public boolean checkToken(long token) {
		for (Login s:getSessionSet())
			if (s.compareToken(token))
				return false;
		return true;
	}
	
	public Login getLogin(long token) throws InvalidSessionException{
		Login login = null;
		for (Login l : super.getSessionSet()){
			if (l.getToken() == token)
				login = l;
		}
	   if (login == null)
		   throw new InvalidSessionException();
	   
	   else {
		   login.isValid();
		   return login;
		}
	}
	
	public void cleanInvalidTokens(){
		for (Login l : super.getSessionSet()){
			try {
				l.isValid();
			}
			catch (InvalidSessionException e){
				l.remove();
			}
				
		}
	}
		
}
