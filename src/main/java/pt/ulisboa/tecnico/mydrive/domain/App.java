package pt.ulisboa.tecnico.mydrive.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.ulisboa.tecnico.mydrive.exception.FileExistsException;
import pt.ulisboa.tecnico.mydrive.exception.InconsistentContentException;
import pt.ulisboa.tecnico.mydrive.exception.PermissionDeniedException;

public class App extends App_Base {
    
    public App() {
        super();
    }

	public App(String name) {
		setName(name);
	}
	
	public App(Directory pai, String name, String content, User user, int idf){
		pai.verificapath(name);
		if (user.getUsername().equals(pai.getOnwer().getUsername()) 
				| user.getUsername().equals("root") 
				| pai.getMask().charAt(5)=='r')
    	{
			if (content!=null)
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
    	}
		else throw new PermissionDeniedException();
	}
	
	@Override
	public String getType(){
		return "a";
	}
	
	public boolean checkContent(String content) throws InconsistentContentException{
		if (content.contains("/") | content.contains("\0") ){
			throw new InconsistentContentException();
		}

		else return true;
	}
	
	@Override
	public Element xmlExport(){
		Element element = new Element("app");
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
	public void writeFile(String content){
		checkContent(content);
		setContent(content);		
	}
	
	@Override
	public void execute(String[] args, User user) throws NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if (user.equals(getOnwer()) | user.getUsername().equals("root") | user.getUmask().charAt(6)==('x') && !(user.getUsername().equals("nobody") )){
			String name = getContent();
			 Class<?> cls;
			    Method meth;
			    try { // name is a class: call main()
			      cls = Class.forName(name);
			      meth = cls.getMethod("main", String[].class);
			    } catch (ClassNotFoundException cnfe) { // name is a method
			      int pos;
			      if ((pos = name.lastIndexOf('.')) < 0) throw cnfe;
			      cls = Class.forName(name.substring(0, pos));
			      meth = cls.getMethod(name.substring(pos+1), String[].class);
			    }
			    meth.invoke(null, (Object)args); // static method (ignore return)
		}
		else
			throw new PermissionDeniedException();
	}

}
