package org.icea.swim.ws.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Flight {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idFlight;

	private String flightNumber;

	private String organ;
	
	@OneToOne
	private Aerodrome addep;

	private String origin;

	@OneToOne
	private Aerodrome addes;

	private String destination;

	private Date date;

	private String route;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<Airway> airwaysList;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<AirspaceCoordinateFix> fixList;

	private String airlineEnterpriseCode;

	private String airlineEnterpriseName;

	private String flightLevel;

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}


	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}


	public Aerodrome getAddep() {
		return addep;
	}

	public void setAddep(Aerodrome addep) {
		this.addep = addep;
	}

	public Aerodrome getAddes() {
		return addes;
	}

	public void setAddes(Aerodrome addes) {
		this.addes = addes;
	}

	public void setAirwaysList(Set<Airway> airwaysList) {
		this.airwaysList = airwaysList;
	}

	public void setFixList(Set<AirspaceCoordinateFix> fixList) {
		this.fixList = fixList;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getAirlineEnterpriseCode() {
		return airlineEnterpriseCode;
	}

	public void setAirlineEnterpriseCode(String airlineEnterpriseCode) {
		this.airlineEnterpriseCode = airlineEnterpriseCode;
	}

	public String getAirlineEnterpriseName() {
		return airlineEnterpriseName;
	}

	public void setAirlineEnterpriseName(String airlineEnterpriseName) {
		this.airlineEnterpriseName = airlineEnterpriseName;
	}

	public String getFlightLevel() {
		return flightLevel;
	}

	public void setFlightLevel(String flightLevel) {
		this.flightLevel = flightLevel;
	}

	public int getIdFlight() {
		return idFlight;
	}

	public List<Airway> getAirwaysList() {
		return new ArrayList<Airway>(airwaysList);
	}

	public void setAirwaysList(List<Airway> airwaysList) {
		this.airwaysList = new HashSet<Airway>(airwaysList);
	}
	
	public List<AirspaceCoordinateFix> getFixList() {
		return new ArrayList<AirspaceCoordinateFix>(fixList);
	}

	public void setFixList(List<AirspaceCoordinateFix> fixList) {
		this.fixList = new HashSet<AirspaceCoordinateFix>(fixList);
	}


}
