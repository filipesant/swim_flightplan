package org.icea.swim.ws;

import javax.xml.ws.Endpoint;

public class PublishService {
	
	 public static void main(String[] args) {

	        FlightPlanWS service = new FlightPlanWS();
	        
	        String URL = "http://localhost:8080/enrouteweatherservice";

	        System.out.println("Service Running: " + URL);

	        Endpoint.publish(URL, service);
	    }

}
