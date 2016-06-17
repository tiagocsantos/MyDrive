package pt.ulisboa.tecnico.mydrive.presentation;

import pt.ulisboa.tecnico.mydrive.service.*;

public class ChangeWorkingDirectory extends MdCommand {

	public ChangeWorkingDirectory(Shell sh) {
		super(sh, "cwd", "Change Working Directory");
	}

	@Override
	public void execute(String[] args) {
		if (args.length < 1)
		    throw new RuntimeException("USAGE: "+name()+" token path");
		
		else{
			String path=args[0];
			ChangeDirectoryService service = null;
			service = new ChangeDirectoryService(shell().getCurrentToken(), path);
			service.execute();
			System.out.println(service.result());
		}

	}

}
