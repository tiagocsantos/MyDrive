package pt.ulisboa.tecnico.mydrive.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.ulisboa.tecnico.mydrive.exception.FileExistsException;
import pt.ulisboa.tecnico.mydrive.exception.PermissionDeniedException;


public class FileText extends FileText_Base {
    
    public FileText() {
        super();
    }

	public FileText(String name) {
		if(CheckString(name))
			setName(name);
	}
	
	public FileText(Directory pai, String name, String content, User user, int idf){
		pai.verificapath(name);
		if (user.equals(pai.getOnwer()) | user.getUsername().equals("root") | pai.getMask().charAt(5)=='r')
    	{
			if (pai.checkExistence(name))
	    		throw new FileExistsException(name);
			
			if(CheckString(name)){
				setDirectory(pai);
				setName(name);
				setContent(content);
				setOnwer(user);
				setIdf(idf);
				setMask(user.getUmask());
				setLastmod(new DateTime());
			}
    	}
		else throw new PermissionDeniedException();
	}
	
	@Override
	public String getType(){
		return "f";
	}
	
	public int xmlImport(Element node, User user, Raiz raiz) throws FileExistsException {

		int idf = Integer.parseInt(node.getAttributeValue("idf"));
		setIdf(idf);
		
		setOnwer(user);
		
		String mask = node.getChildText("mask");
		if (mask == null)
			mask = "rwed----";
		setMask(mask);
		
		setLastmod(new DateTime());
		
		setContent(node.getChildText("content"));
		
		String path = node.getChildText("path");
		
		Directory current = raiz.processPathCreate(path, user, raiz.getMydrive());
		if (!current.checkExistence(getName()))
			setDirectory(current);
		else
			throw new FileExistsException(getName());
		
		return idf;
	}
	
	@Override
	public Element xmlExport(){
		Element element = new Element("textfile");
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
	public int getSize() {
		return getContent().length();
	}
	
	@Override
	public String readFile(){
		return this.getContent();
	}
	@Override
	public void writeFile(String content){
		setContent(content);
	}
	@Override
	public void remove(){
		setDirectory(null);
		setOnwer(null);
		deleteDomainObject();
	}
	
	@Override
	public void execute (String[] args, User user) throws NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		FileText exe = null;
		exe = getOnwer().getAssociation(getName());
		if (exe!=null){
			exe.execute(getContent().split(" "), user);
			return;
		}
		if (user.equals(getOnwer()) | user.getUsername().equals("root") | user.getUmask().charAt(6)==('x') && !(user.getUsername().equals("nobody") )){		
		//arguments will be ignored
			String[] linhas = getContent().split("\n");
			for (String linha : linhas){
				String[] info = linha.split(" ");
				getDirectory().processPath(info[0]).execute(Arrays.copyOfRange(info, 1, info.length), user);
			}
		}
		else
			throw new PermissionDeniedException();
	}
	
}
