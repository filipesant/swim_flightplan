package org.icea.swim.ws.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import aero.fixm.flight.AircraftType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomList {

	@XmlElement(name="aircraft")
	private List<AircraftType> itens;
	
	public CustomList() {
	}
	
	public CustomList(List<AircraftType> itens){
		
		this.itens = itens;
	}
	
	public List<AircraftType> getAircrafts(){
		
		return itens;
		
	}
}
