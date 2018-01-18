package org.icea.swim.ws.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jws.WebService;
import javax.persistence.EntityManager;

import org.icea.swim.ws.config.JPAConfiguration;
import org.icea.swim.ws.dao.AirspaceCoordinateFixDao;
import org.icea.swim.ws.dao.AirwayDao;
import org.icea.swim.ws.dao.FlightDao;
import org.icea.swim.ws.data.importer.UploadData;
import org.icea.swim.ws.filter.FlightPlanFilter;
import org.icea.swim.ws.mock.AircraftMock;
import org.icea.swim.ws.model.AirspaceCoordinateFix;
import org.icea.swim.ws.model.Flight;
import org.icea.swim.ws.utils.GeolocationConveter;
import org.icea.swim.ws.wrapper.CustomList;

import aero.fixm.base.GeographicalPositionType;
import aero.fixm.base.IcaoAerodromeReferenceType;
import aero.fixm.base.PositionPointType;
import aero.fixm.base.TimeType;
import aero.fixm.flight.AircraftType;
import aero.fixm.flight.ArrivalType;
import aero.fixm.flight.BoundaryCrossingType;
import aero.fixm.flight.DepartureType;
import aero.fixm.flight.EnRouteType;
import aero.fixm.flight.FlightIdentificationType;
import aero.fixm.flight.FlightType;

@WebService
public class FlightController {

	EntityManager entityManager;

	AirspaceCoordinateFixDao daoAirspaceCoordinateFix;

	AirwayDao daoAirway;

	FlightDao daoFlight;

	GeolocationConveter converter;

	public FlightController() {

		entityManager = JPAConfiguration.getEntityManager("flight_info");

		daoAirspaceCoordinateFix = new AirspaceCoordinateFixDao();

		daoAirway = new AirwayDao();

		daoFlight = new FlightDao();

		converter = new GeolocationConveter();

		daoAirspaceCoordinateFix.setEntityManager(entityManager);

		daoAirway.setEntityManager(entityManager);

		daoFlight.setEntityManager(entityManager);

	}

	public CustomList getAircrafts(){

		AircraftMock mock = new AircraftMock();

		List<AircraftType> aircrafts = mock.getAircrafts();

		return new CustomList(aircrafts);

	}

	public boolean loadDatabase() throws IOException, ParseException{

		UploadData upLoad = new UploadData();
		System.out.println("DIR: "+this.getClass().getClassLoader().getResource("META-INF/data/Fixos_Planilha.csv").getPath());
		
		upLoad.saveAirspaceCoordinateFix(this.getClass().getClassLoader().getResource("META-INF/data/Fixos_Planilha.csv").getPath());
		upLoad.saveAirway(this.getClass().getClassLoader().getResource("META-INF/data/AIRWAYS.csv").getPath());
		upLoad.saveAeroDrome(this.getClass().getClassLoader().getResource("META-INF/data/AERODROME.csv").getPath());
		upLoad.saveFlight(this.getClass().getClassLoader().getResource("META-INF/data/SETA_VOO_FEB_TO_AUG_2017.csv").getPath());
		
		return true;

	}

	public ArrayList<FlightType> getFlightPlans(FlightPlanFilter filter){

		String query = "SELECT f FROM Flight as f ";

		if(!filter.getQuery().isEmpty()) {

			query += " WHERE " + filter.getQuery();
		}

		System.out.println("QUERY : "+query);

		List<Flight> flightList = daoFlight.getSpecificQuery(query);
		
		System.out.println("SIZE QUERY" + flightList.size());

		ArrayList<FlightType> flightTypeList = new ArrayList<FlightType>();

		for (Flight flight : flightList) {

			FlightType flightType = new FlightType();

			ArrivalType arrival = new ArrivalType();

			/** Setting Origin **/

			IcaoAerodromeReferenceType originAerdrome = new IcaoAerodromeReferenceType();

			originAerdrome.setLocationIndicator(flight.getDestination());

			arrival.setAerodrome(originAerdrome);

			flightType.setArrival(arrival);


			/** Setting Destination **/

			DepartureType departure = new DepartureType();

			IcaoAerodromeReferenceType destinationAerodrome = new IcaoAerodromeReferenceType();

			destinationAerodrome.setLocationIndicator(flight.getOrigin());

			departure.setAerodrome(destinationAerodrome);
			
			TimeType timeDep = new TimeType();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				
			
			Pattern pattern = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]");
			Matcher matcher = pattern.matcher(flight.getDate().toGMTString());

			String dateResult = dateFormat.format(flight.getDate()); 
			
			if (matcher.find()){
				
			    dateResult = dateResult.concat(" "+matcher.group());
			}
			
			timeDep.setTimeReference(dateResult);
			
			departure.setActualTimeOfDeparture(timeDep);
			
			flightType.setDeparture(departure);

			/** Setting Flight Identification **/

			FlightIdentificationType flightIdentification = new FlightIdentificationType();

			flightIdentification.setAircraftIdentification(flight.getFlightNumber());

			flightType.setFlightIdentification(flightIdentification);			


			/** Setting Flight WayPoints **/

			EnRouteType enRouteType = new EnRouteType();

			BoundaryCrossingType boundaryCross = new BoundaryCrossingType();

			PositionPointType positionType = new PositionPointType();

			GeographicalPositionType geoPosition = new GeographicalPositionType();

			geoPosition.setSrsName("LatitudeLongitude");

			/** Origin**/

			geoPosition.getPos().add(converter.DMStoDecimalAerodrome(flight.getAddep().getLatitude()));

			geoPosition.getPos().add(converter.DMStoDecimalAerodrome(flight.getAddep().getLongitude()));

			/** Route Fix**/

			for(AirspaceCoordinateFix fix : flight.getFixList()) {

				String latitude = fix.getLatitude();

				String longitude = fix.getLongitude();

				geoPosition.getPos().add(converter.DMStoDecimal(latitude));

				geoPosition.getPos().add(converter.DMStoDecimal(longitude));
			}

			/** Destination**/

			geoPosition.getPos().add(converter.DMStoDecimalAerodrome(flight.getAddes().getLatitude()));

			geoPosition.getPos().add(converter.DMStoDecimalAerodrome(flight.getAddes().getLongitude()));


			positionType.setPosition(geoPosition);

			boundaryCross.setCrossingPoint(positionType);

			enRouteType.setBoundaryCrossingCoordination(boundaryCross);
			
			flightType.setEnRoute(enRouteType);

			flightTypeList.add(flightType);

		}

		System.out.println("flightTypeList SIZE"+flightTypeList.size());
		
		return flightTypeList ;

	}



}
