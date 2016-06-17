package pt.ulisboa.tecnico.mydrive.presentation;

public class ChangeSession extends MdCommand {

	public ChangeSession(Shell sh) {
		super(sh, "token" ,"Change Session");
	}

	@Override
	public void execute(String[] args) {
		if (args.length < 1)
		    throw new RuntimeException("USAGE: "+name()+" username");
		
		else{
			long token = shell().getToken(args[0]);
			shell().setCurrentToken(token);
		}

	}

}
