package pt.ulisboa.tecnico.mydrive.presentation;

import pt.ulisboa.tecnico.mydrive.service.ListDirectoryService;
import pt.ulisboa.tecnico.mydrive.service.dto.DirectoryDto;

public class ListDirectory extends MdCommand {

	public ListDirectory(Shell sh) {
		super(sh, "ls","List the Directory");
	}

	@Override
	public void execute(String[] args) {
		ListDirectoryService service = null;
		if (args.length == 0){
			service = new ListDirectoryService(shell().getCurrentToken(), ".");
			service.execute();
			for (DirectoryDto d: service.result()){
				System.out.println(d.export());
				//System.out.println(d.get_type() +" "+ d.get_permission() +" "+ d.get_size() +"\t"+ d.get_owner() +"\t"+ d.get_idf() +"\t"+ d.get_lastMod() +" "+ d.get_name());
			}		
		}
		
		else if (args.length == 1) {
			String path=args[0];
			service = new ListDirectoryService(shell().getCurrentToken(), path);
			service.execute();
			for (DirectoryDto d: service.result()){
				System.out.println(d.export());
						}
		}
		
		else throw new RuntimeException("USAGE: "+name()+" [path]");
	}

}
