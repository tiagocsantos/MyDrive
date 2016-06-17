package pt.ulisboa.tecnico.mydrive.domain;

import java.lang.reflect.InvocationTargetException;

import org.jdom2.Element;
import org.jdom2.Parent;
import org.joda.time.DateTime;

import pt.ulisboa.tecnico.mydrive.exception.FileDoesNotExistException;
import pt.ulisboa.tecnico.mydrive.exception.FileExistsException;
import pt.ulisboa.tecnico.mydrive.exception.InconsistentContentException;
import pt.ulisboa.tecnico.mydrive.exception.PermissionDeniedException;

public class Link extends Link_Base {
    
    public Link() {
        super();
    }

	public Link(String name) {
		setName(name);
	}
	
	public Link(Directory pai, String name, String content, User user, int idf){
		pai.verificapath(name);
		if (user.equals(pai.getOnwer()) || user.getUsername().equals("root") || pai.getMask().charAt(5)=='r')
    	{
			checkContent(content);
			if (pai.checkExistence(name))
	    		throw new FileExistsException(name);
			
			setName(name);
			setOnwer(user);
			setDirectory(pai);
			setContent(content);
			setIdf(idf);
			setLastmod(new DateTime());
			setMask(user.getUmask());
			if(!checkLoop(pai, content, name)){
				remove();
				throw new InconsistentContentException();
			}
    	}
		else throw new PermissionDeniedException();
	}
	
	public boolean checkContent(String content){
		if (content.contains("\\") || content.contains("[") ||content.contains(".")|| content.contains("]") || content.contains("\0") || content=="" ){
			throw new InconsistentContentException();
		}
		else return true;
	}
	
	@Override
	public String getType(){
		return "l";
	}
	
	@Override
	public Element xmlExport(){
		Element element = new Element("link");
		element.setAttribute("idf", getIdf().toString());
		Element nameElement= new Element("name");
		nameElement.setText(getName());
		element.addContent(nameElement);
		
		Element ownerElement= new Element("owner");
		ownerElement.setText(getOnwer().getUsername());
		element.addContent(ownerElement);
		
		Element maskElement= new Element("mask");
		maskElement.setText(getMask());
		element.addContent(maskElement);
		
		Element pathElement = new Element("path");
		pathElement.setText(getPath());
		element.addContent(pathElement);
		
		Element contentElement= new Element("content");
		contentElement.setText(getContent());
		element.addContent(contentElement);
		
		return element;
	}
	
	@Override
	public void execute(String args[], User user) throws NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if (user.equals(getOnwer()) | user.getUsername().equals("root") | user.getUmask().charAt(6)==('x') && !(user.getUsername().equals("nobody") )){
			Files file = getLinkedFile();
			file.execute(args, user);
		}
		else 
			throw new PermissionDeniedException();
	}
	
	@Override
	public String readFile(){
		Files file = getLinkedFile();
		return file.readFile();
	}
	
	@Override
	public void writeFile(String content){
		Files file = getLinkedFile();
		file.writeFile(content);
	}
	
	public Files getLinkedFile(){
		String link = getContent();
		return getDirectory().processPath(link);
	}
	
	public boolean checkLoop(Directory pai,String content,String name){
		String path=pai.getPath();
		path+="/"+name;
		try{
			Files file=pai.processPath(content);
			//System.out.println(((Link)file).getContent());
			//System.out.println(path);
			if(file==this)
				return false;
			
			if (file instanceof Link){
				((Link) file).getContent().equals(path);
				return false;
			}
				
		}
		catch (FileDoesNotExistException e){
			return true;
		}
		return true;
	}
}
