package net.langenmaier.strohstern.data.storage.dto;

import net.langenmaier.strohstern.data.storage.model.Target;
import net.langenmaier.strohstern.data.storage.enumeration.SearchState;

public class JsonNavigationState {
	public Double angle;
	public Double geo_distance;
	
	public Target target;
	public SearchState searchState;
}
