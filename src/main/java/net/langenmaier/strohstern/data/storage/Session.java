package net.langenmaier.strohstern.data.storage;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Session extends PanacheEntity {

	@Column(unique = true)
	public String uuid = UUID.randomUUID().toString();
	
	public Double longitude;
	public Double latitude;
	public String status;

	public OffsetDateTime updatedAt;

	public static Session findByUuid(UUID uuid){
		return find("uuid", uuid.toString()).firstResult();
	}

}
