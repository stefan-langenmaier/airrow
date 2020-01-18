package net.langenmaier.strohstern.data.storage;

public class GeoTools {

	public static double getBearing(double latStart, double longStart, double latEnd, double longEnd) {
		return Math.atan2(Math.cos(latStart)*Math.sin(latEnd)-Math.sin(latStart)*Math.cos(latEnd)*Math.cos(longEnd-longStart),
				Math.sin(longEnd-longStart)*Math.cos(latEnd));
	}
	
	public static double getWebAngle(double angle) {
		return (360 + (90-angle))%360;
	}
	
}
