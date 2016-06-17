package pt.ulisboa.tecnico.mydrive.domain;

public class Nobody extends Nobody_Base {
	
	public Nobody(MyDrive md){
		setUsername("nobody");
		setName("Guest");
	    setUmask("rwxdr-x-");
	    setPassword("");
	    md.setVisitor(this);
	    Directory home = (Directory) md.getRaiz().getInsideDir("home");
	    Directory main = new Directory(home, this, 2);
	    setMainDir(main);
	}
}
