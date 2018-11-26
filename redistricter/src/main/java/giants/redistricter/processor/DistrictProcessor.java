package giants.redistricter.processor;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import giants.redistricter.data.District;

public class DistrictProcessor {
	
	public Collection<District> processDistricts(Collection<gerrymandering.model.District> districts){
		Collection<District> toReturn = new TreeSet<>(Comparator.comparing(District::getId));
		giants.redistricter.data.District process;
		for(gerrymandering.model.District d : districts) {
			process = new District();
			process.setId(d.getDistrictId());
			toReturn.add(process);
		}
		return toReturn;
	}

}
