package net.langenmaier.strohstern.data.storage.helper;

import net.langenmaier.strohstern.data.storage.model.Location;

public class GeoTools {

	public static double getBearing(double latStart, double longStart, double latEnd, double longEnd) {
		return Math.atan2(Math.cos(latStart)*Math.sin(latEnd)-Math.sin(latStart)*Math.cos(latEnd)*Math.cos(longEnd-longStart),
				Math.sin(longEnd-longStart)*Math.cos(latEnd));
	}
	
	public static double getWebAngle(double angle) {
		return (360 + (angle-90))%360;
	}

	// https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
	public static Double getDistance(Location s, Location t) {
		
		final int R = 6371; // Radius of the earth

		double latDistance = Math.toRadians(t.lat - s.lat);
		double lonDistance = Math.toRadians(t.lon - s.lon);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(Math.toRadians(s.lat)) * Math.cos(Math.toRadians(t.lat))
				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c * 1000; // convert to meters
	
		return distance;
	}

	public static Double getWebAngle(Location s, Location t) {
		return getWebAngle(Math.toDegrees(GeoTools.getBearing(s.lat, s.lon, t.lat, t.lon)));
	}
	
}
