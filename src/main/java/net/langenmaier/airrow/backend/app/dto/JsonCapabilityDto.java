package net.langenmaier.airrow.backend.app.dto;

import net.langenmaier.airrow.backend.app.model.Capability;

public class JsonCapabilityDto {
    public Boolean canUpload;

    public static JsonCapabilityDto of(Capability c) {
		JsonCapabilityDto jc = new JsonCapabilityDto();

		jc.canUpload = c.canUpload;

		return jc;
	}
}
