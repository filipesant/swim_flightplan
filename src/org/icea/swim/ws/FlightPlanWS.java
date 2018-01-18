package org.icea.swim.ws;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.icea.swim.ws.controller.FlightController;
import org.icea.swim.ws.filter.FlightPlanFilter;

import aero.fixm.flight.FlightType;

@WebService
@SOAPBinding(style=Style.DOCUMENT)
public class FlightPlanWS {

	@WebMethod(operationName = "loadDatabase")
	@WebResult(name = "result")
	public boolean loadDatbase() {

		FlightController controller = new FlightController();
		boolean result = false;
		
		try {
			result = controller.loadDatabase();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	@WebMethod(operationName = "getFlightPlans")
	@WebResult(name = "flights")
	public ArrayList<FlightType> getFlightPlans(@WebParam(name="filter") FlightPlanFilter filter) {
		FlightController controller = new FlightController();
		return controller.getFlightPlans(filter);
	}

}
