package net.langenmaier.airrow.backend.app.rule;

import net.langenmaier.airrow.backend.app.dto.JsonPersonalInformation;

public class CanUploadRule {

    public static Boolean hasPermission(JsonPersonalInformation jpi) {
        int minTraces = 50;
        if ((jpi.trajectoryPoints - (jpi.pointsPoints*minTraces)) > minTraces) {
            return true;
        }
        return false;
    }

}
