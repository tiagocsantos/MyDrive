package pt.ulisboa.tecnico.mydrive.presentation;

import pt.ulisboa.tecnico.mydrive.service.LoginUserService;

public class Login extends MdCommand {

	public Login(Shell sh) {
		super(sh, "login", "Log a new user in the system");
	}

	@Override
	public void execute(String[] args) {
		if (args.length < 1)
		    throw new RuntimeException("USAGE: "+name()+" username [password]");
		
		LoginUserService service = null;
		
		if (args.length == 1)
			service = new LoginUserService(args[0], args[0]);
		
		else if (args.length == 2)
			service = new LoginUserService(args[0], args[1]);
		
		service.execute();
		shell().addToken(args[0], service.result());
		if (shell().getCurrentToken()==0)
			shell().setCurrentToken(service.result());
		
		if (shell().getCurrentToken() == shell().getToken("nobody"))
			shell().setCurrentToken(service.result());

	}

}
