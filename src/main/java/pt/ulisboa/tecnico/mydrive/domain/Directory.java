package pt.ulisboa.tecnico.mydrive.domain;
import java.util.*;

import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.ulisboa.tecnico.mydrive.exception.*;

public class Directory extends Directory_Base {
    
    public Directory(String name, Directory pai, User owner, int idf) {
    	if (owner.equals(pai.getOnwer()) | owner.getUsername().equals("root") | pai.getMask().charAt(5)=='r')
    	{
    		if (checkExistence(name))
	    		throw new FileExistsException(name);
	    	
	    	if(CheckString(name)){
		    	setName(name);
		        setOnwer(owner);
		        setDirectory(pai);
		        setLastmod(new DateTime());
		        setIdf(idf);
		        setMask(owner.getUmask());
	        }
    	}
    	else throw new PermissionDeniedException();
    }
    //para criar homedirs
    public Directory(Directory pai, User owner, int idf){
    	setName(owner.getUsername());
        setOnwer(owner);
        setDirectory(pai);
        setLastmod(new DateTime());
        setIdf(idf);
        setMask(owner.getUmask());
    	
    }
    public Directory(){
    	super();
    }
    
	public Directory(String name) {
		if (CheckString(name))
			setName(name);
	}
	
	@Override
	public String getType(){
		return "d";
	}
	
	public int xmlImport(Element node, User owner, Raiz raiz) throws FileExistsException {
		setOnwer(owner);
		
		String mask = node.getChildText("mask");
		if (mask==null)
			mask = "rwed----";
		setMask(mask);
		
		setLastmod(new DateTime());	
		
		int idf = Integer.parseInt(node.getAttributeValue("idf"));
		setIdf(idf);
		
		String path = node.getChildText("path");
		MyDrive md = raiz.getMydrive();
		Directory current = (Directory) raiz.processPathCreate(path, owner, raiz.getMydrive());
		if (!current.checkExistence(getName()))
			setDirectory(current);
		else
			throw new FileExistsException(getName());
		
		return idf;
	}
	
	public Files getInsideDir(String name) throws FileDoesNotExistException {
		if (name.equals("."))
			return this;
		
		else if (name.equals(".."))
			return getDirectory();
		
		else {
			for (Files f : getFichsSet()){
				if (f.getName().equals(name))
					return f;
			}
		}
		throw new FileDoesNotExistException(name);	
	}
	
	public boolean checkExistence(String name){
		for (Files f : getFichsSet()){
			if (f.getName().equals(name))
				return true;
		}

		return false;
	}

	//Listagem da diretoria : devolve uma lista com os nomes dos ficheiros existentes na diretoria
	public void ListDir(){
		Iterator<Files> ite = getFichsSet().iterator();
		Files file;
		String fileName;
		System.out.println("ls: "+getName());
		while (ite.hasNext()){
			file = ite.next();
			fileName = file.getName();
			System.out.println(fileName);
		}	
	}

	@Override
	public void remove() {
		for (Files f : getFichsSet()){
			f.remove();
		}
		
    	setDirectory(null);
    	setOnwer(null);
    	setHomeowner(null);
    	deleteDomainObject();
    }
	
	@Override
	public Element xmlExport(){
		Element element = new Element("directory");
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
		
		
		return element;
	}
	
	public ArrayList<Files> getInsideFiles(){
		ArrayList<Files> files = new ArrayList<Files>();
		Iterator<Files> in = getFichsSet().iterator();
		while (in.hasNext()){
			files.add(in.next());
		}
		return files;
	}
	
	@Override
	public boolean isHomeDir(){
		return getHomeowner() != null;
	}
	
	public Files processPath(String path){
		int i,s=0;
		
		checkPath(path);
		Files current = this;
		if (path.startsWith("/")){
			s=1;
			current = this.getOnwer().getMyDrive().getRaiz();
		}
	
		String[] caminho = path.split("/");
		for (i=s;i<caminho.length; i++){
			if (current instanceof Directory){
				//System.out.println(caminho[i]);
				current = ((Directory) current).getInsideDir(caminho[i]);
				
			}
			else if (current instanceof Link)
				current=((Link)current).getLinkedFile();
			else {
				return current;
			}
		}
		return current;
	}
	
	public void checkPath(String path) throws InvalidPathException{
		if(path.equals("") |path.contains("\\")| path.contains("\0")){
			throw new InvalidPathException();
		}
	}
	
	public Directory processPathCreate(String path, User user, MyDrive md){
		int i;
		Directory current = this;
		String[] caminho = path.split("/");
		for (i=1;i<caminho.length; i++){
			try {
				current = (Directory) current.getInsideDir(caminho[i]);
			}
			catch (FileDoesNotExistException e){
				if (i== caminho.length-1)
					current = new Directory(current, user, md.getIDF());
				else
					current = new Directory(caminho[i], current, md.getSuperuser(), md.getIDF());
			}
		}
		return current;
	}
	public int getSize() {
		return getInsideFiles().size();
	}
	
	@Override
	public String readFile(){
		throw new NotATextFileException(this.getName());
	}
	
	@Override 
	public void writeFile(String content){
		throw new NotATextFileException(getName());
	}
	
	@Override
	public void execute(String[] args, User user){
		throw new NotATextFileException(getName());
	}
	
	public void remove(String _name, User user) {
		Files d = getInsideDir(_name);
		if (d.getOnwer().equals(user)| user.getUsername().equals("root")){
			d.remove();
		}
		else if (d.getMask().endsWith("d"))
				d.remove();
			
		else 
			throw new PermissionDeniedException();
	}
	
	
}
