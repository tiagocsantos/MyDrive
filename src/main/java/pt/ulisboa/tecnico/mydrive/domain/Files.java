package pt.ulisboa.tecnico.mydrive.domain;

import pt.ulisboa.tecnico.mydrive.exception.*;

import java.lang.reflect.InvocationTargetException;

import org.jdom2.Element;
import org.joda.time.DateTime;

public class Files extends Files_Base {
    
    public Files(String name, User owner, Directory pai, int idf) {
    	if(CheckString(name)){
    	  setName(name);
          setOnwer(owner);
          setDirectory(pai);
          setLastmod(new DateTime());
          setIdf(idf);
          setMask("rwxd----");
    	}
    }

	public Files() {
		// TODO Auto-generated constructor stub
	}

	public String getType(){
		return null;
	}
    /*
	public Files processPath(String path, Directory raiz){
		int i;
		Files current = raiz;
		String[] caminho = path.split("/");
		for (i=1;i<caminho.length; i++){
			if (current instanceof Directory){
				current = ((Directory) current).getInsideDir(caminho[i]);
			}
			//else if (current instanceof Link)
				//do stuff
			else return current;
		}
		return current;
	}
	*/
	public void remove(){
	//DO NOTJING
	}
	
	public Boolean CheckString(String toBeChecked) throws InvalidFileNameException{
		
		if (toBeChecked.equals("") | toBeChecked.contains("/") | toBeChecked.contains("\0")){
			throw new InvalidFileNameException();
		}
		else 
			return true;
	}
	
	public String getPath(){
		String path= "";
		Directory current = getDirectory();
		while (!(current instanceof Raiz)){
			if (path=="")
				path = current.getName();
			else
				path =  current.getName()+"/"+ path;
			current = current.getDirectory();
		}
		if (!path.equals(""))
			path = "/"+path;

		return path+ "/" + getName();
	}


	public Element xmlExport() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isHomeDir() {
		return false;
	}
	
	public String readFile(){
		return null;
	}

	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void execute(String[] _args, User user) throws NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		
	}

	public void writeFile(String content) {
		// TODO Auto-generated method stub
		
	}
	
	public void verificapath(String name) throws InvalidPathException{
		if ((getPath()+name).length()>1024){
			throw new InvalidPathException();
		}
	}
}
