package pt.ulisboa.tecnico.mydrive.presentation;

import java.util.Arrays;

import pt.ulisboa.tecnico.mydrive.service.*;

public class ExecuteFile extends MdCommand {
	
	public ExecuteFile(Shell sh) {
		super(sh, "do", "Execute a file");
	}

	@Override
	public void execute(String[] args) {
		if (args.length < 1){
			throw new RuntimeException("USAGE: "+name()+" path [args]");
		}
		ExecuteFileService service = null;
		String s = "";
		if (args.length > 1)
			s = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		
		service = new ExecuteFileService(shell().getCurrentToken(),args[0],s);
		service.execute();
		
	}

	

}
