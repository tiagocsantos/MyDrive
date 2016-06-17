package pt.ulisboa.tecnico.mydrive.presentation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public abstract class Shell {
	  protected static final Logger log = LogManager.getRootLogger();
	  private Map<String,Command> coms = new TreeMap<String,Command>();
	  private PrintWriter out;
	  private String name;
	  private long currentToken=0;
	  private Map<String, Long> tokens = new TreeMap<String, Long>();
	
	  public Shell(String n) { this(n, new PrintWriter(System.out, true), true); }
	  public Shell(String n, Writer w) { this(n, w, true); }
	  public Shell(String n, Writer w, boolean flush) {
	    name = n;
	    out = new PrintWriter(w, flush);
	
	    new Command(this, "quit", "Quit the command interpreter") {
	      void execute(String[] args) {
		System.out.println(name+" quit");
	        System.exit(0);
	      }
	    };

	    new Command(this, "help", "this command help") {
	      void execute(String[] args) {
		if (args.length == 0) {
		  for (String s: shell().list()) System.out.println(s);
		  System.out.println(name()+" name (for command details)");
		} else {
		  for (String s: args)
		    if (shell().get(s) != null)
		      System.out.println(shell().get(s).help());
		}
	      }
	    };
	  }
	
	  public void print(String s) { out.print(s); }
	  public void println(String s) { out.println(s); }
	  public void flush() { out.flush(); }
	
	  // false if it redefines an existing Command
	  /* package */ boolean add(Command c) {
	      return coms.put(c.name(), c) == null ? true : false;
	  }
	  public Command get(String s) {
	      return coms.get(s);
	  }
	  public Collection<String> list() {
	      return Collections.unmodifiableCollection(coms.keySet());
	  }
	
	  public void execute() throws Exception {
	    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String str, prompt = null; // System.getenv().get("PS1");
	
	    if (prompt == null) prompt = "$ ";
	    System.out.println(name+" shell ('quit' to leave)");
	    System.out.print(prompt);
	    while ((str = in.readLine()) != null) {
	      String[] arg = str.split(" ");
	      Command c = coms.get(arg[0]);
	      if (c != null) {
		try {
		  c.execute(Arrays.copyOfRange(arg, 1, arg.length));
		} catch (RuntimeException e) {
		  System.err.println(arg[0]+": "+e);
		  e.printStackTrace(); // debug
		}
	      } else
		if (arg[0].length() > 0)
	          System.err.println(arg[0]+": command not found. ('help' for command list)");
	      System.out.print(prompt);
	    }
	    System.out.println(name+" end");
	  }
	
	  public long getCurrentToken() {
		return currentToken;
	  }
	  
	  public void setCurrentToken(long currentToken) {
		this.currentToken = currentToken;
	  }
	  
	  public void addToken(String user, long token){
		  tokens.put(user, token);
	  }
	  
	  public long getToken(String user){
		  return tokens.get(user);
	  }
}
