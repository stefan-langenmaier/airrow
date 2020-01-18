package net.langenmaier.strohstern.data.storage;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class TrajectoryPoint extends PanacheEntity {

	public Double direction;
	public Double longitude;
	public Double latitude;
	
	public OffsetDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(name="sessionId")
	public Session session;
	
	public static TrajectoryPoint of(UpdateData ud) {
		TrajectoryPoint tp = new TrajectoryPoint();
		tp.direction = ud.direction;
		tp.longitude = ud.longitude;
		tp.latitude = ud.latitude;
		ZoneOffset zoneOffSet= ZoneOffset.of("+00:00");
		tp.updatedAt = OffsetDateTime.now(zoneOffSet);
		return tp;
	}

	public void refresh(Session s) {
		this.session = s;
		s.latitude = latitude;
		s.longitude = longitude;
		s.updatedAt = updatedAt;
		
	}

}
