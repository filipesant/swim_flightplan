package org.icea.swim.ws.controller;

import java.util.List;

import org.icea.swim.ws.mock.AircraftMock;
import org.icea.swim.ws.wrapper.CustomList;

import aero.fixm.flight.AircraftType;

public class AircraftController {
	
	public CustomList getAircrafts(){
		
		AircraftMock mock = new AircraftMock();
		
		List<AircraftType> aircrafts = mock.getAircrafts();
		
		return new CustomList(aircrafts);
		
	}

}
