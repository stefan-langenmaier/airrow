package net.langenmaier.strohstern.data.storage;

public class Direction {
	public Double angle;
	public String status;
	public Target target = null;
	
	public Direction(double angle, String status) {
		this.angle = angle;
		this.status = status;
	}

}
