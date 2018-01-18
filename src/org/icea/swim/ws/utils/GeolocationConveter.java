package org.icea.swim.ws.utils;

import java.text.DecimalFormat;

public class GeolocationConveter {

	public double DMStoDecimal(String dms) {

		String direction = "";

		String[] dmsCoord = dms.split(" ");

		double degree = 0, minute = 0, second = 0 , result = 0;

		for(int i = 0 ; i < dmsCoord.length ; i++ ) {

			if(i == 0) {
				direction = dmsCoord[i];
			}

			if(i == 1) {
				degree = Double.parseDouble(dmsCoord[i]);
			}

			if(i == 2) {
				minute = Double.parseDouble(dmsCoord[i])/60;
			}

			if(i == 3) {
				second = Double.parseDouble(dmsCoord[i])/3600;
			}
		}

		result = degree + minute + second;

		if (direction.equals("S") || direction.equals("W") ) {

			result *= -1;
		}

		String truncate =  new DecimalFormat("#.#######").format(result);

		truncate = truncate.replace(",",".");
		result = Double.parseDouble(truncate);

		return result;

	}

	public double DMStoDecimalAerodrome(String dms) {

		String direction = "";

		dms = dms.replace("'","").replaceAll("\"","").replaceAll("°", "");
		
		String[] dmsCoord = dms.split(" ");

		double degree = 0, minute = 0, second = 0 , result = 0;

		for(int i = 0 ; i < dmsCoord.length ; i++ ) {

			if(i == 0) {
				degree = Double.parseDouble(dmsCoord[i]);
			}

			if(i == 1) {
				minute = Double.parseDouble(dmsCoord[i])/60;
			}

			if(i == 2) {
				second = Double.parseDouble(dmsCoord[i])/3600;
			}

			if(i == 3) {
				direction = dmsCoord[i];
				
			}
		}

		result = degree + minute + second;

		if (direction.equals("S") || direction.equals("W") ) {

			result *= -1;
		}

		String truncate =  new DecimalFormat("#.#######").format(result);
		truncate = truncate.replace(",",".");
		result = Double.parseDouble(truncate);

		return result;

	}

}
