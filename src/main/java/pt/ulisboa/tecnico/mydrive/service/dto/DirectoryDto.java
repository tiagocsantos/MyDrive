package pt.ulisboa.tecnico.mydrive.service.dto;

import org.joda.time.DateTime;

import pt.ulisboa.tecnico.mydrive.domain.User;

public class DirectoryDto implements Comparable<DirectoryDto> {
	private String _name;
	private String _owner;
	private String _type;
	private int _size;
	private String _permission;
	private int _idf;
	private DateTime _lastMod;
	private String _content = "";
	
	public DirectoryDto(String name, String owner, String type, int size, String permission, int idf, DateTime lastMod, String content){
		_name = name;
		_owner = owner;
		_type = type;
		_size = size;
		_permission = permission;
		_idf = idf;
		_lastMod = lastMod;	
		_content = content;
	}
	
	public DirectoryDto(String name, String owner, String type, int size, String permission, int idf, DateTime lastMod){
		_name = name;
		_owner = owner;
		_type = type;
		_size = size;
		_permission = permission;
		_idf = idf;
		_lastMod = lastMod;	

	}
	
	public String get_owner() {
		return _owner;
	}

	public String get_name() {
		return _name;
	}


	public String get_type() {
		return _type;
	}

	public int get_size() {
		return _size;
	}

	public String get_permission() {
		return _permission;
	}

	public int get_idf() {
		return _idf;
	}

	public DateTime get_lastMod() {
		return _lastMod;
	}
	
	@Override
	public int compareTo(DirectoryDto arg0) {
		return  get_name().compareTo(arg0.get_name());
	}

	public String export() {
		String ret = get_type()+"\t"+get_permission()+"\t"+get_size()+"\t"+get_owner();
		if (get_owner().length()<5) ret+="\t";
		ret+="\t"+get_idf()+"\t"+get_lastMod()+"\t"+get_name();
		return ret;
	}
}
