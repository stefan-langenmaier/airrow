package net.langenmaier.strohstern.data.storage.dto;

import net.langenmaier.strohstern.data.storage.model.Capability;

public class JsonCapabilityDto {
    public Boolean canUpload;

    public static JsonCapabilityDto of(Capability c) {
		JsonCapabilityDto jc = new JsonCapabilityDto();

		jc.canUpload = c.canUpload;

		return jc;
	}
}
