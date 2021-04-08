package net.langenmaier.strohstern.data.rule;

import net.langenmaier.strohstern.data.storage.dto.JsonPersonalInformation;

public class CanUploadRule {

    public static Boolean hasPermission(JsonPersonalInformation jpi) {
        int minTraces = 50;
        if ((jpi.trajectoryPoints - (jpi.pointsPoints*minTraces)) > minTraces) {
            return true;
        }
        return false;
    }

}
