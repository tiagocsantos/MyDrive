package pt.ulisboa.tecnico.mydrive.service.system;

import org.junit.Test;

import pt.ulisboa.tecnico.mydrive.presentation.*;
import pt.ulisboa.tecnico.mydrive.service.AbstractServiceTest;


public class SystemTest extends AbstractServiceTest {

    private MdShell sh;

    protected void populate() {
    	sh = new MdShell();
    }

    @Test
    public void success() {
    	new ImportXML(sh).execute(new String[] { "info/esxml.xml" } );
        new Login(sh).execute(new String[] { "tiago123" } );
        new ChangeSession(sh).execute(new String[] {"tiago123"});
        new ExecuteFile(sh).execute(new String[] { "/home/tiago123/cenas/app1" } );
        new AddVariable(sh).execute(new String[] { "var1", "var2" } );
        new ChangeWorkingDirectory(sh).execute(new String[] { "cenas/cenas2" } );
        new WriteFile(sh).execute(new String[] { "text2", "test" } );
        new ListDirectory(sh).execute(new String[] {"/home/tiago123/cenas" } );
    }
}