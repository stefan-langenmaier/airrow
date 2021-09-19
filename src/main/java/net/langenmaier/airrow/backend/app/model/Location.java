package net.langenmaier.airrow.backend.app.model;

public class Location {
	public Double lat;
	public Double lon;

	public Location() {}

	public Location(Location l) {
		lat = l.lat;
		lon = l.lon;
	}
}
