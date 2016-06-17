package pt.ulisboa.tecnico.mydrive.domain;

import java.util.ArrayList;
import java.util.Iterator;

import org.joda.time.DateTime;

public class Raiz extends Raiz_Base {
    
    public Raiz(Root root) {
    	setOnwer(root);
    	setName("/");
        setIdf(0);
        setMask("rwx-----");
        setLastmod(new DateTime());
        setDirectory(this);
    }
    
    public ArrayList<Files> getInsideFiles(){
		ArrayList<Files> files = new ArrayList<Files>();
		Iterator<Files> in = getFichsSet().iterator();
		Files file = null;
		while (in.hasNext()){
			file = in.next();
			if (!file.getName().equals("/"))
				files.add(file);
		}
		return files;
	}
    
}
