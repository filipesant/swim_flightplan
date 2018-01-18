package org.icea.swim.ws.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class AirspaceCoordinateFix {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idAirspaceCoordinateFix;
	
	private String fix;
	
	private String latitude;
	
	private String longitude;

	public int getId() {
		return idAirspaceCoordinateFix;
	}

	public void setId(int id) {
		this.idAirspaceCoordinateFix = id;
	}

	public String getFix() {
		return fix;
	}

	public void setFix(String fix) {
		this.fix = fix;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	
}