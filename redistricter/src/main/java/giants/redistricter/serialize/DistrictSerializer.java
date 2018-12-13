package giants.redistricter.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import giants.redistricter.algorithm.Move;
import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class DistrictSerializer extends JsonSerializer<District> {

    @Override
    public void serialize(District district, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Set<Integer> precinctIds = new LinkedHashSet<>();
        for (Precinct p : district.getPrecincts()) {
            precinctIds.add(p.getId());
        }
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", district.getDistrictId());
        jsonGenerator.writeObjectField("precincts", precinctIds);
        jsonGenerator.writeEndObject();
    }
}
