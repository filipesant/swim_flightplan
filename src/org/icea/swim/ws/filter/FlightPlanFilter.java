package org.icea.swim.ws.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class FlightPlanFilter {

	String startDateTime;

	String finalDateTime;

	String aerodromeOrigin;

	String aerodromeDestination;

	public String getQuery(){

		List<Object> list =  new ArrayList<Object>();

		String timeStampInitial = startDateTime;

		String timeStampFinal = finalDateTime;

		String query = "";

		if(!aerodromeOrigin.isEmpty() && !aerodromeOrigin.contains("?")) {

			HashMap<String, String> map = new HashMap<String, String>();

			map.put("origin",aerodromeOrigin);

			list.add(map);
		}

		if(!aerodromeDestination.isEmpty() && !aerodromeDestination.contains("?")) {

			HashMap<String, String> map = new HashMap<String, String>();

			map.put("destination",aerodromeDestination);

			list.add(map);
		}
		

		if(!timeStampInitial.isEmpty() && !timeStampInitial.contains("?") && timeStampInitial != null && !timeStampInitial.contains("  ")) {

			HashMap<String, String> map = new HashMap<String, String>();

			map.put("dateInitial",timeStampInitial);
			
			if(!timeStampFinal.isEmpty() && !timeStampFinal.contains("?")) {
				
				map.put("dateFinal",timeStampFinal);
			}

			list.add(map);
		}


		for(int i = 0 ; i < list.size() ; i++) {

			HashMap<String, String> condition = (HashMap<String, String>) list.get(i);

			if(condition.containsKey("origin")) {

				query += " f.origin = '" + condition.get("origin") +"'";

			} else if(condition.containsKey("destination")) {

				query += " f.destination = '" + condition.get("destination") +"'";

			} else if(condition.containsKey("dateInitial")) {

				query += " f.date BETWEEN '" + condition.get("dateInitial") +"' AND '"+condition.get("dateFinal") +"'";

			} 

			if( i != list.size() - 1) {

				query += " AND ";

			}


		}

		return query;
	}


	public String getStartDateTime() {
		return startDateTime;
	}


	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}


	public String getFinalDateTime() {
		return finalDateTime;
	}


	public void setFinalDateTime(String finalDateTime) {
		this.finalDateTime = finalDateTime;
	}


	public String getAerodromeOrigin() {
		return aerodromeOrigin;
	}

	public void setAerodromeOrigin(String aerodromeOrigin) {
		this.aerodromeOrigin = aerodromeOrigin;
	}

	public String getAerodromeDestination() {
		return aerodromeDestination;
	}

	public void setAerodromeDestination(String aerodromeDestination) {
		this.aerodromeDestination = aerodromeDestination;
	}



}
