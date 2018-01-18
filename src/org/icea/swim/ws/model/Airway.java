package org.icea.swim.ws.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Airway {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idAirway;
	
	private String airway;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<AirspaceCoordinateFix> fix;
	
		
	public List<AirspaceCoordinateFix> getFix() {
		return new ArrayList<AirspaceCoordinateFix>(fix);
	}

	public void setFix(List<AirspaceCoordinateFix> fix) {
		this.fix = new HashSet<AirspaceCoordinateFix>(fix);
	}

	public String getAirway() {
		return airway;
	}

	public void setAirway(String airway) {
		this.airway = airway;
	}

	public int getIdAirwayCoordinateFix() {
		return idAirway;
	}
	
	
	
	
}
