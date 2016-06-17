package pt.ulisboa.tecnico.mydrive.domain;

public class Root extends Root_Base {
    
    public Root(MyDrive md) {
        setUsername("root");
        setName("Super User");
        setUmask("rwxdr-x-");
        setPassword("***");
        Raiz raiz = new Raiz(this);
        md.setRaiz(raiz);
        md.setSuperuser(this);
        Directory home = new Directory("home", raiz, this, 1);
        Directory main = new Directory("root", home, this, 2);
        setMainDir(main);
    }
    
}
