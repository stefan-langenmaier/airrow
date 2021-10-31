package net.langenmaier.airrow.backend.app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.langenmaier.airrow.backend.app.helper.GeoTools;

class GeoToolsTest {

	@Test
	void testGetBearing() {
		double latStart = Math.toRadians(50);
		double longStart = Math.toRadians(5);
		
		double latEnd = Math.toRadians(58);
		double longEnd = Math.toRadians(5);
		
		
		double degrees = Math.toDegrees(GeoTools.getBearing(latStart, longStart, latEnd, longEnd));
		assertTrue(Math.abs(90d - degrees) < 0.00001);
		
		latStart = Math.toRadians(50);
		longStart = Math.toRadians(5);
		
		latEnd = Math.toRadians(50);
		longEnd = Math.toRadians(10);
		
		
		degrees = Math.toDegrees(GeoTools.getBearing(latStart, longStart, latEnd, longEnd));
		assertTrue(Math.abs(2d - degrees) < 0.1);
	}
	
	@Test
	void testWebAngle() {
		assertTrue(Math.abs(GeoTools.getWebAngle(0) - 90) < 0.00001);
		assertTrue(Math.abs(GeoTools.getWebAngle(90) - 0) < 0.00001);
		assertTrue(Math.abs(GeoTools.getWebAngle(10) - 80) < 0.00001);
		assertTrue(Math.abs(GeoTools.getWebAngle(180) - 270) < 0.00001);
	}

}
