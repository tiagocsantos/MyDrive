package pt.ulisboa.tecnico.mydrive.presentation;

import pt.ulisboa.tecnico.mydrive.service.ImportXMLService;

public class ImportXML extends MdCommand {

	public ImportXML(Shell sh) {
		super(sh, "import", "Import XML document");
	}

	@Override
	public void execute(String[] args) {
		if (args.length < 1)
		    throw new RuntimeException("USAGE: "+name()+" File Name");
		String nomeFicheiro=args[0];
		ImportXMLService service = null;
		service = new ImportXMLService(nomeFicheiro);
		service.execute();
	}

}
