package pt.ulisboa.tecnico.mydrive.presentation;

import pt.ulisboa.tecnico.mydrive.service.AddVarService;
import pt.ulisboa.tecnico.mydrive.service.dto.VarDto;

public class AddVariable extends MdCommand {

	public AddVariable(Shell sh) {
		super(sh, "env", "Create a new ambient variable");
	}

	@Override
	public void execute(String[] args) {
		AddVarService service = null ;
		if (args.length == 0){
			service = new AddVarService(shell().getCurrentToken(), "", "");
			service.execute();
			for (VarDto v: service.result()){
				System.out.println(v.getName()+"="+v.getValue());
			}
			
		}
				
		else if (args.length == 1){
			service = new AddVarService(shell().getCurrentToken() ,args[0], "");
			service.execute();
			for (VarDto v: service.result()){
				if (v.getName().equals(args[0]))
					System.out.println(v.getName()+"="+v.getValue());
			}
		}
		
		else if (args.length == 2){
			service = new AddVarService(shell().getCurrentToken() ,args[0], args[1]);
			service.execute();
		}
		
		else throw new RuntimeException("USAGE: "+name()+" [name [value]]");
	
	}

}
