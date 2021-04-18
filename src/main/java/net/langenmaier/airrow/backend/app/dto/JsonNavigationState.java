package net.langenmaier.airrow.backend.app.dto;

import net.langenmaier.airrow.backend.app.enumeration.SearchState;
import net.langenmaier.airrow.backend.app.model.Target;

public class JsonNavigationState {
	public Double angle;
	public Double geo_distance;
	
	public Target target;
	public SearchState searchState = SearchState.SEARCHING;
	public JsonCapabilityDto capability;
}
