package pt.ulisboa.tecnico.mydrive.service.system;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

import pt.ulisboa.tecnico.mydrive.domain.FileText;
import pt.ulisboa.tecnico.mydrive.exception.FileDoesNotExistException;
import pt.ulisboa.tecnico.mydrive.service.*;
import pt.ulisboa.tecnico.mydrive.service.dto.DirectoryDto;
import pt.ulisboa.tecnico.mydrive.service.dto.VarDto;

@RunWith(JMockit.class)
public class IntegrationTest extends AbstractServiceTest {
	private static final String importFile = "info/esxml.xml"; 
	private static final String file ="text2", content="Textfile 2 para ver", filetext = "textfile", path ="/home/tiago123/cenas/cenas2";
	private static final String changeContent="test";
	@Override
	protected void populate() {

	}
	
	@Test
	public void sucess() throws Exception{
		new ImportXMLService(importFile).execute();
		LoginUserService result = new LoginUserService("tiago123","tiago123");
		result.execute();
		long tk = result.result();
		
		new CreateFileService(tk,file, filetext).execute();
		
		
		ListDirectoryService list = new ListDirectoryService(tk, path);
		list.execute();
		List<DirectoryDto> directories =list.result();
		assertEquals(3, directories.size());
		
		ChangeDirectoryService dir = new ChangeDirectoryService(tk,path);
		dir.execute();
		String caminho = dir.result();
		assertEquals(path, caminho);


		ReadFileService service = new ReadFileService(tk, file);
		service.execute();
		String conteudo = service.result();
		assertEquals( content, conteudo);
		
		new ChangeDirectoryService(tk,"/home/tiago123/cenas").execute();

		new ExecuteFileService(tk,"/home/tiago123/cenas/app1", "1 2 3 4").execute();
		
		AddVarService var = new AddVarService(tk, "var1", "var2");
		var.execute();
		List<VarDto> variable = var.result();
		assertEquals(1,variable.size());
		
		new WriteFileService(tk, "textfile1", changeContent).execute();
		
		ReadFileService newService = new ReadFileService(tk, "textfile1");
		newService.execute();
		String conteudoAlterado = newService.result();
		assertEquals( changeContent, conteudoAlterado);
		
		new DeleteFileService(tk, "textfile1").execute();
		
		ReadFileService service1 = new ReadFileService(tk, "textfile1");
		boolean check= false;
		try{
			service1.execute();
			
		}catch (FileDoesNotExistException e){
			check = true;
		}
		
		assertTrue(check);
	}
	
	
}
