package org.icea.swim.ws.data.importer;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.icea.swim.ws.config.JPAConfiguration;
import org.icea.swim.ws.dao.AerodromeDao;
import org.icea.swim.ws.dao.AirspaceCoordinateFixDao;
import org.icea.swim.ws.dao.AirwayDao;
import org.icea.swim.ws.dao.FlightDao;
import org.icea.swim.ws.model.Aerodrome;
import org.icea.swim.ws.model.AirspaceCoordinateFix;
import org.icea.swim.ws.model.Airway;
import org.icea.swim.ws.model.Flight;
import org.icea.swim.ws.utils.GeolocationConveter;


public class UploadData {

	EntityManager entityManager;

	AirspaceCoordinateFixDao daoAirspaceCoordinateFix;

	AirwayDao daoAirway;
	
	FlightDao daoFlight;
	
	AerodromeDao daoAerodrome;
	
	GeolocationConveter converter;

	public UploadData() {

		entityManager = JPAConfiguration.getEntityManager("flight_info");

		daoAirspaceCoordinateFix = new AirspaceCoordinateFixDao();
		
		daoAirway = new AirwayDao();
		
		daoFlight = new FlightDao();
		
		daoAerodrome = new AerodromeDao();
		
		converter = new GeolocationConveter();

		daoAirspaceCoordinateFix.setEntityManager(entityManager);
		
		daoAirway.setEntityManager(entityManager);
		
		daoFlight.setEntityManager(entityManager);
		
		daoAerodrome.setEntityManager(entityManager);

	}
	
	public void test() {
		
		String origin = "SÃO PAULO";
		
		String destination = "BELO HORIZONTE";
		
		String date = "2015-08-11 10:53:12";
		
		String query = "SELECT f FROM Flight as f WHERE f.origin = '" + origin + "' and f.destination = '" + destination + "' and f.date = '" + date +"'";
				
		List<Flight> test = daoFlight.getSpecificQuery(query);
		
//		List<Flight> test =  daoFlight.getFilterOneParameter("id",Integer.toString(9));
		System.out.println("TESTE");
		
		for (Flight flight : test) {
			
			System.out.println("Flight: "+ flight.getFlightNumber());
			
			System.out.println(flight.getAddep() + " "+ flight.getAddes());
			
			for(AirspaceCoordinateFix fix : flight.getFixList()) {
				
				
				
				System.out.println("FIX : "+ new GeolocationConveter().DMStoDecimal(fix.getLatitude()) +","+ new GeolocationConveter().DMStoDecimal(fix.getLongitude()));
			}
			
//			for (Airway airway : flight.getAirwaysList()) {
////				
//				System.out.println("Name : "+ airway.getAirway());
////				
//				for (AirspaceCoordinateFix fix : airway.getFix()) {
//					
//					System.out.println("AIRWAY FIX : "+ fix.getFix());
//					
//				}
////				
//			}
			
		}
		
		
		for (Aerodrome aerodrome : daoAerodrome.getAll()) {
			
			System.out.println(converter.DMStoDecimalAerodrome(aerodrome.getLatitude()));
			
		}
		
	}

	public Boolean saveAirspaceCoordinateFix(String file) throws IOException{

		System.out.println("STARTED AirspaceCoordinateFix");
		
		Reader in = new FileReader(file);

		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);

		records.iterator().next();

		for (CSVRecord record : records) {

			AirspaceCoordinateFix model =  new AirspaceCoordinateFix();

			model.setFix(record.get(0));

			model.setLatitude(record.get(1));

			model.setLongitude(record.get(2));
			
			entityManager.getTransaction().begin();

			daoAirspaceCoordinateFix.save(model);
			
			entityManager.getTransaction().commit();
			
			
		}

		System.out.println("FINISHED AirspaceCoordinateFix");
		return true;

	}

	public Boolean saveAirway(String file) throws IOException{

		System.out.println("STARTED saveAirway");
		
		Reader in = new FileReader(file);

		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		
		records.iterator().next();
		
		for (CSVRecord record : records) {
			
			String airway = record.get(0).toString();
			
			String fix = record.get(1).toString();
			
			List<Airway> listAirway = daoAirway.getFilterOneParameter("airway", airway);
			
			if(listAirway.isEmpty()) {
				
				Airway model = new Airway();
				
				model.setAirway(airway);				
				
				List<AirspaceCoordinateFix> airspaceFixList = daoAirspaceCoordinateFix.getFilterOneParameter("fix",fix);
				
				model.setFix(airspaceFixList);
				
				entityManager.getTransaction().begin();
				
				daoAirway.save(model);
				
				entityManager.getTransaction().commit();
				
				
			} else {
				
				Airway model = listAirway.get(0);
				
				List<AirspaceCoordinateFix> airspaceFixList = model.getFix();
				
				try {
					
					AirspaceCoordinateFix airspaceFix = daoAirspaceCoordinateFix.getFilterOneParameter("fix",fix).get(0);
					
					airspaceFixList.add(airspaceFix);
					
					model.setFix(airspaceFixList);
					
					entityManager.getTransaction().begin();
					
					daoAirway.save(model);
					
					entityManager.getTransaction().commit();
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			
		}
		
		System.out.println("FINISHED saveAirway");
		return true;

	}
	
	public void saveFlight(String file) throws IOException, ParseException {
		
		System.out.println("STARTED saveFlight");
		
		Reader in = new FileReader(file);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		records.iterator().next();
		
		for (CSVRecord record : records) {
			
			String flightNumber = record.get(0).toString();
			
			String organ = record.get(1).toString();
			
			String addep = record.get(2).toString();
			
			String origin = record.get(3).toString();
			
			String addes = record.get(4).toString();
			
			String destination = record.get(5).toString();
			
			String dateRaw = record.get(6).toString();
			
			String route = record.get(7).toString();
			
			String enterpriseCode = record.get(8).toString();
			
			String enterpriseName = record.get(9).toString();
			
			Flight model = new Flight();
			
			model.setFlightNumber(flightNumber);
			
			model.setOrgan(organ);
			
			try {
			
				Aerodrome dep = daoAerodrome.getFilterOneParameter("icaoCode", addep).get(0);
				
				model.setAddep(dep);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			model.setOrigin(origin);

			try {
			
				Aerodrome des = daoAerodrome.getFilterOneParameter("icaoCode", addes).get(0);
				
				model.setAddes(des);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			model.setDestination(destination);
			
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
				
				Date date = sdf.parse(dateRaw);
				
				model.setDate(date);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			model.setRoute(route);
			
			List<Airway> airwayList = new ArrayList<Airway>();
			
			List<AirspaceCoordinateFix> fixList = new ArrayList<AirspaceCoordinateFix>();
			
			String[] airways = route.split(" ");
			
			for (int i = 0 ; i < airways.length ; i++) {
				
				try {
				
					Airway modelAirway = daoAirway.getFilterOneParameter("airway", airways[i]).get(0);
					
					airwayList.add(modelAirway);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {
					
					AirspaceCoordinateFix fix = daoAirspaceCoordinateFix.getFilterOneParameter("fix", airways[i].replaceAll(" ","")).get(0);
					
					fixList.add(fix);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
			}
			
			model.setAirwaysList(airwayList);
			
			model.setFixList(fixList);
			
			model.setAirlineEnterpriseCode(enterpriseCode);
			
			model.setAirlineEnterpriseName(enterpriseName);
			
			if(airwayList.size() > 0 || fixList.size() > 0) {

				entityManager.getTransaction().begin();

				daoFlight.save(model);

				entityManager.getTransaction().commit();
			}
			
		}
		
		System.out.println("FINISHED saveFlight");
		
	}
	
	public void saveAeroDrome(String file) throws IOException {
		
		System.out.println("STARTED saveAerodrome");
		
		Reader in = new FileReader(file);

		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		
		records.iterator().next();
		
		for (CSVRecord record : records) {
			
			try {
				
				String code = record.get(0).toString();
				
				String name = record.get(2).toString();
				
				String latitude = record.get(5).toString();
				
				String longitude = record.get(6).toString();
				
				Aerodrome model = new Aerodrome();
				
				model.setIcaoCode(code);
				
				model.setName(name);
				
				model.setLatitude(latitude);
				
				model.setLongitude(longitude);
				
				entityManager.getTransaction().begin();

				daoAerodrome.save(model);

				entityManager.getTransaction().commit();
				
				
			}catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		System.out.println("FINISHED saveAerodrome");
		
	}


	public void testDate(String file) throws IOException, ParseException {
		
		Reader in = new FileReader(file);

		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		
		if(records instanceof Collection<?>) {
			System.out.println("SIZE: "+((Collection<?>)records).size());
		}
		
		records.iterator().next();
		
		for (CSVRecord record : records) {
			
			try {
				
				String dateRaw = record.get(6).toString();
//				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//				
				Date date = sdf.parse(dateRaw);
				
				System.out.println(date.toString());
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
	}
	
	public static void main(String[] args) throws Exception{

		UploadData upLoad = new UploadData();
		upLoad.saveAirspaceCoordinateFix(upLoad.getClass().getClassLoader().getResource("META-INF/data/Fixos_Planilha.csv").getPath());
		upLoad.saveAirway(upLoad.getClass().getClassLoader().getResource("META-INF/data/AIRWAYS.csv").getPath());
		upLoad.saveAeroDrome(upLoad.getClass().getClassLoader().getResource("META-INF/data/AERODROME.csv").getPath());
		upLoad.saveFlight(upLoad.getClass().getClassLoader().getResource("META-INF/data/SETA_VOO_FEB_TO_AUG_2017.csv").getPath());
		
		

	}

}
