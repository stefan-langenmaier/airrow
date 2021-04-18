package net.langenmaier.airrow.backend.app.model;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import net.langenmaier.airrow.backend.app.dto.EsCapabilityDto;
import net.langenmaier.airrow.backend.app.dto.JsonPersonalInformation;
import net.langenmaier.airrow.backend.app.rule.CanUploadRule;

public class Capability {
    public Boolean canUpload = false;
    public OffsetDateTime updatedAt;

    public static Capability of(JsonPersonalInformation jpi) {
		Capability c  = new Capability();
        c.canUpload = CanUploadRule.hasPermission(jpi);

        ZoneOffset zoneOffSet= ZoneOffset.of("+00:00");
		c.updatedAt = OffsetDateTime.now(zoneOffSet);
        
        return c;
    }

    public static Capability of(EsCapabilityDto ec) {
        Capability c  = new Capability();
        c.canUpload = ec.canUpload;
        c.updatedAt = ec.updatedAt;

        return c;
    }

    public static Capability empty() {
        return new Capability();
    }
}
