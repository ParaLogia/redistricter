package giants.redistricter.processor;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import giants.redistricter.data.District;

public class DistrictProcessor {
	
	public Set<District> processDistricts(Set<gerrymandering.model.District> districts){
		Set<District> toReturn = new LinkedHashSet<>();
		giants.redistricter.data.District process;
		for(gerrymandering.model.District d : districts) {
			process = new District();
			process.setId(d.getDistrictId());
			process.setBorder(d.getBoundaryJSON());
			toReturn.add(process);
		}
		return toReturn;
	}

}
