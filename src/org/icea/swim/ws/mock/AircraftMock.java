package org.icea.swim.ws.mock;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import aero.fixm.flight.AircraftType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD )
public class AircraftMock {
	
	public List<AircraftType> getAircrafts(){
		
		List<AircraftType> aircraftList =  new ArrayList<AircraftType>();
		
		for(int i = 1; i < 6 ; i++) {
			
			AircraftType type = new AircraftType();
			
			type.setAircraftAddress("ADDRESS "+i);
			type.setRegistration("REGISTRATION "+i);
			type.setSignificantMarkings("MARK "+i);
			type.setNumberOfAircraft(i);
			
			aircraftList.add(type);
			
		}
		
		return aircraftList;
		
	}

}
