package pt.ulisboa.tecnico.mydrive.service;

import pt.ulisboa.tecnico.mydrive.domain.*;
import pt.ulisboa.tecnico.mydrive.exception.*;

public class WriteFileService extends MyDriveService {
	long _token;
	private String fileName;
	private String content;
	public WriteFileService(long token, String nome, String conteudo) {
		_token = token;
		fileName = nome;
		content = conteudo;
	}

	@Override
	protected void dispatch() throws MyDriveException, PermissionDeniedException, LinkFileException, NotATextFileException {
		MyDrive md=getMyDrive();
		Login user = md.getLogin(_token);
		
		Directory currentDir = user.getCurrentDir();
		Files ficheiro = currentDir.getInsideDir(fileName);
		/*
		if(ficheiro.getType().equals("l")){
			throw new LinkFileException();
		}
		
		if (ficheiro.getType().equals("d")){
			throw new NotATextFileException(ficheiro.getName());
		}
		*/
		if (ficheiro.getOnwer().equals(user.getSessionOwner())||user.getSessionOwner().getUsername().equals("root")
				|| (user.getSessionOwner().getUmask().charAt(5) == ('w') && !(user.getSessionOwner().getUsername().equals("nobody") )))
			ficheiro.writeFile(content);
		
		else
			throw new PermissionDeniedException();
	}

}
