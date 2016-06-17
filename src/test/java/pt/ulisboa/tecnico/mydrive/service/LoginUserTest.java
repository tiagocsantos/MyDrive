package pt.ulisboa.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.ulisboa.tecnico.mydrive.domain.Directory;
import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.domain.User;
import pt.ulisboa.tecnico.mydrive.exception.IncorrectPasswordException;
import pt.ulisboa.tecnico.mydrive.exception.UserDoesNotExistException;

public class LoginUserTest extends AbstractServiceTest {
	MyDrive md;
	@Override
	protected void populate() {
		md = MyDrive.getInstance();
		
		new User(md, "migs", "Miguel", "12345678");
		
	}
	
	
	@Test(expected = IncorrectPasswordException.class)
	public void wrongPass(){
		LoginUserService l1 = new LoginUserService("migs", "abdc");
		l1.execute();
	}
	
	@Test(expected = UserDoesNotExistException.class)
	public void wrongUser(){
		LoginUserService l1 = new LoginUserService("user213", "asd12123123da");
		l1.execute();
	}
	
	@Test
	public void loginOK(){
		LoginUserService l1 =new LoginUserService("migs", "12345678");
		l1.execute();
		long token  = l1.result();
		Login login = md.getLogin(token);
		Directory dir = login.getCurrentDir();
		assertEquals("migs", dir.getName());
	}
}
