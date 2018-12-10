package giants.redistricter.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import giants.redistricter.algorithm.Move;
import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;

import java.io.IOException;

public class MoveSerializer extends JsonSerializer<Move> {

    @Override
    public void serialize(Move move, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Precinct precinct = move.getPrecinct();
        District srcDistrict = move.getSourceDistrict();
        District destDistrict = move.getDestinationDistrict();

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("precinct", precinct.getId());
        jsonGenerator.writeNumberField("sourceDistrict", srcDistrict.getDistrictId());
        jsonGenerator.writeNumberField("destinationDistrict", destDistrict.getDistrictId());
        jsonGenerator.writeEndObject();
    }
}
