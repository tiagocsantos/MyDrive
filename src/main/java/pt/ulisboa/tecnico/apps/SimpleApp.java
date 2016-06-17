package pt.ulisboa.tecnico.apps;

public class SimpleApp {
	
	public static void main(String args[]){
		System.out.println("Executing main");
		for (String s : args)
			System.out.println(s);
		System.out.println("Done");
	}
	
	public static void SimpleMethod(String args[]){
		System.out.println("Executing Simple Method");
		for (String s : args)
			System.out.println(s);
		System.out.println("Done");
	}
}
