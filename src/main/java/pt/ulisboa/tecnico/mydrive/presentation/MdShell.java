package pt.ulisboa.tecnico.mydrive.presentation;


public class MdShell extends Shell {

	public static void main(String[] args) throws Exception {
		MdShell sh;
		if (args.length >0)
			sh = new MdShell(args[0]);
		else 
			sh = new MdShell();
		
	  sh.execute();
	}
	
	public MdShell(String file) { // add commands here
	  super("MyDrive");
	  new ChangeSession(this);
	  ImportXML x = new ImportXML(this);
	  new ExecuteFile(this);
	  new ListDirectory(this);
	  new AddVariable(this);
	  new WriteFile(this);
	  new ExecuteFile(this);
	  new ChangeWorkingDirectory(this);
	  Login l = new Login(this); 
	  l.execute(new String[] {"nobody", ""});
	  x.execute(new String[] {file});
	 
	}

	public MdShell() {
		 super("MyDrive");
		  new ChangeSession(this);
		  ImportXML x = new ImportXML(this);
		  new ExecuteFile(this);
		  new ListDirectory(this);
		  new AddVariable(this);
		  new WriteFile(this);
		  new ExecuteFile(this);
		  new ChangeWorkingDirectory(this);
		  Login l = new Login(this); 
		  l.execute(new String[] {"nobody", ""});
		 
	}

}
