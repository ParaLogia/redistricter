package giants.redistricter.processor;

import java.util.Collection;
import java.util.HashSet;

import gerrymandering.model.District;

public class DistrictProcessor {
	
	public Collection<giants.redistricter.data.District> processDistricts(Collection<District> districts){
		Collection<giants.redistricter.data.District> toReturn = new HashSet<giants.redistricter.data.District>();
		giants.redistricter.data.District process;
		for(District d : districts) {
			process = new giants.redistricter.data.District();
			process.setId(d.getDistrictId());
			toReturn.add(process);
		}
		return toReturn;
	}

}
