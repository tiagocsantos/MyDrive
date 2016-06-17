package pt.ulisboa.tecnico.mydrive.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.domain.User;
import pt.ulisboa.tecnico.mydrive.exception.InvalidSessionException;
import pt.ulisboa.tecnico.mydrive.service.dto.VarDto;


public class AddVarServiceTest extends AbstractServiceTest {

	private long tokentiago;
	private long tokenmario;
	
	@Override
	protected void populate() {
		MyDrive md = MyDrive.getInstance();
		
		new User(md, "tiago", "Tiago Santos", "12345678");
		new User(md,"mario","Mario Santos", "12345678");
		
		Login l1 = new Login("tiago", "12345678", md, false);
		Login l2 = new Login("mario","12345678",md,true);
		
		l1.addVar("existente","teste");
		
		tokentiago = l1.getToken();
		tokenmario = l2.getToken();
		
	}
	
	@Test
	public void newAmbVar(){
		AddVarService addVarS = new AddVarService(tokentiago,"cenas","valorcenas");
		addVarS.execute();
		List<VarDto> result = addVarS.result();
		assertEquals(2,result.size());	
	}
	
	@Test
	public void editVar(){
		AddVarService addVarS = new AddVarService(tokentiago,"existente","ola");
		addVarS.execute();
		List<VarDto> result = addVarS.result();
		assertEquals(1,result.size());	
	}
	
	@Test(expected=InvalidSessionException.class)
	public void InvalidSession(){
		AddVarService addVarS = new AddVarService(tokenmario,"erro","-1");
		addVarS.execute();
	}

}
