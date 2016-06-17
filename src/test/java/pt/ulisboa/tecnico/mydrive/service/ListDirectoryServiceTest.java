package pt.ulisboa.tecnico.mydrive.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import pt.ulisboa.tecnico.mydrive.domain.App;
import pt.ulisboa.tecnico.mydrive.domain.Directory;
import pt.ulisboa.tecnico.mydrive.domain.FileText;
import pt.ulisboa.tecnico.mydrive.domain.Link;
import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.domain.User;
import pt.ulisboa.tecnico.mydrive.exception.FileDoesNotExistException;
import pt.ulisboa.tecnico.mydrive.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.mydrive.service.dto.DirectoryDto;
import pt.ulisboa.tecnico.mydrive.exception.InvalidSessionException;

public class ListDirectoryServiceTest extends AbstractServiceTest {
	
	private long tokentiago;
	private long tokenmario;
	private long tokenNob;
	
	@Override
	protected void populate() {
		MyDrive md = MyDrive.getInstance();
		
		User user = new User(md, "tiago", "Tiago Santos", "12345678");
		new User(md,"mario","Mario Santos", "12345678");
		
		new FileText(user.getMainDir(),"ficheiro","conteudo",user,md.getIDF());
		new App(user.getMainDir(),"aplicacao","conteudo",user,md.getIDF());
		new Link(user.getMainDir(),"link","/home/root",user,md.getIDF());
		new Directory("cenas",user.getMainDir(),user,md.getIDF());
		
		Login l1 = new Login("tiago", "12345678", md, false);
		Login l3 = new Login("nobody","",md,false);
		Login l2 = new Login("mario","12345678",md,true);
		
		l3.setCurrentDir(user.getMainDir());
		tokentiago = l1.getToken();
		tokenmario = l2.getToken();
		tokenNob = l3.getToken();
		
	}
	
	@Test (expected=PermissionDeniedException.class)
	public void testNobody(){
		ListDirectoryService service = new ListDirectoryService(tokenNob,"cenas");
		service.execute(); 
	}
	
	@Test
	public void listEmptyDirectory(){
		ListDirectoryService lis = new ListDirectoryService(tokentiago,"cenas");
		lis.execute();
		List<DirectoryDto> result = lis.result();
		for (DirectoryDto d : result ){
			System.out.println(d.get_name());
		}
		assertEquals(2,result.size());
	}
	
	@Test(expected=FileDoesNotExistException.class)
	public void ListUnexistingDirectory(){
		ListDirectoryService lis = new ListDirectoryService(tokentiago,"naoexiste");
		lis.execute();
	}
	
	@Test
	public void ListDirectory(){
		ListDirectoryService lis = new ListDirectoryService(tokentiago,".");
		lis.execute();
		List<DirectoryDto> result =lis.result();
		assertEquals(6,result.size());
	}
	
	@Test(expected=PermissionDeniedException.class)
	public void ListDirectoryWithoutPermission(){
		ListDirectoryService lis = new ListDirectoryService(tokentiago,"../mario");
		lis.execute();
	}
	
	@Test(expected=InvalidSessionException.class)
	public void InvalidSession(){
		ListDirectoryService lis = new ListDirectoryService(tokenmario,".");
		lis.execute();
	}
}