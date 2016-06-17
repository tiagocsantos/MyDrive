package pt.ulisboa.tecnico.mydrive.presentation;

import pt.ulisboa.tecnico.mydrive.service.*;

public class WriteFile extends MdCommand {

	public WriteFile(Shell sh) {
		super(sh, "update", "write a file");
	}

	@Override
	public void execute(String[] args) {
		if (args.length < 1)
		    throw new RuntimeException("USAGE: "+name()+" <nome> <conteudo>");
		
		if (args.length == 2 )	
			new WriteFileService(shell().getCurrentToken(),args[0], args[1]).execute();
		

	}

}