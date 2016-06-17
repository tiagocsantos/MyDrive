package pt.ulisboa.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.mydrive.domain.Link;
import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.domain.User;
import pt.ulisboa.tecnico.mydrive.exception.PermissionDeniedException;
import pt.ulisboa.tecnico.mydrive.domain.App;
import pt.ulisboa.tecnico.mydrive.domain.Directory;
import pt.ulisboa.tecnico.mydrive.domain.FileText;
import pt.ulisboa.tecnico.mydrive.domain.Files;


@RunWith(JMockit.class)
public class ExecuteFileServiceTest extends AbstractServiceTest {
	
	private static final String pathEnv="/home/$USER/app1";
	private static  Files EnvFile;
	private long token;
	private long tokenNob;
	@Override
	protected void populate() {
		MyDrive md = MyDrive.getInstance();
		User user = new User(md, "sofia", "Sofia Martins", "123456789");
		

		new App(user.getMainDir(), "app1", "pt.ulisboa.tecnico.apps.SimpleApp", user, md.getIDF());

		
		EnvFile=new App(user.getMainDir(), "app2", "pt.ulisboa.tecnico.apps.SimpleApp", user, md.getIDF());

		
		new FileText(user.getMainDir(), "file", "app1 1 2 3\napp1 4 5 6",user, md.getIDF() );
		new FileText(user.getMainDir(), "file.txt", "cenas",user, md.getIDF() );
		
		new Link(user.getMainDir(), "link", "app1", user, md.getIDF());
		
		new Link(user.getMainDir(),"linkEnv", pathEnv, user, md.getIDF());
		
		Login l1 = new Login("sofia", "123456789", md, false);
		Login l2 = new Login("nobody","",md,false);
		l2.setCurrentDir(user.getMainDir());
		token = l1.getToken();
		tokenNob = l2.getToken();	
	}
	
	@Test (expected=PermissionDeniedException.class)
	public void testNobody(){
		ExecuteFileService service = new ExecuteFileService(tokenNob ,"link", "1,2,3");
		service.execute(); 
	}
	
	@Test 
	public void testSimpleApp(){
		ExecuteFileService service = new ExecuteFileService(token, "app1", "1,2,3");
		service.execute();
	}
	
	@Test 
	public void testSimpleFile(){
		ExecuteFileService service = new ExecuteFileService(token, "file", "1,2,3");
		service.execute();
	}
	
	@Test
	 public void testLink(){
		 ExecuteFileService service = new ExecuteFileService(token ,"link", "1,2,3");

		 service.execute(); 

		 service.execute();
	}

	public void executeLinkEnvironment(){
		
		new MockUp<Directory>(){
			@Mock
			Files processPath(String pathEnv){return EnvFile;}
		};


		ExecuteFileService ef=new ExecuteFileService(token,"linkEnv","1,2,3");
		ef.execute();
	}
	
	@Test
	public void executeAssoc(){
		new MockUp<User>(){
			@Mock
			FileText getAssociation(String x) {return  (FileText) EnvFile;}
		};
		
		ExecuteFileService ef=new ExecuteFileService(token,"file.txt","1,2,3");
		ef.execute();
		
	}
}
