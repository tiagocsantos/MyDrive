package pt.ulisboa.tecnico.mydrive.service.dto;

public class VarDto {
	private String name;
	private String value;
	
	public VarDto(String name, String value){
		this.name = name;
		this.value=value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}


}
