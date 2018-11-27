package giants.redistricter.properties;

import org.springframework.boot.json.JacksonJsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class Properties {
    Map<TextProperty,String> props;

    public void loadProps(File file){
        String json;
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            json = br.lines().collect(Collectors.joining());
        } catch (IOException e) {
            return;
        }
        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> map = parser.parseMap(json);

        for (TextProperty prop : TextProperty.values()) {
            try {
                String value = (String) map.get(prop.name());
                props.put(prop, value);
            }
            catch (ClassCastException | NullPointerException ex) {
                 System.err.println("Failed to read property: " + prop);
            }
        }
    }
    public String get(TextProperty prop){
        return props.get(prop);
    }

    public void set(TextProperty prop, String value){
        props.put(prop, value);
    }

    public String export(){
        // TODO write to JSON
        return null;
    }
}
