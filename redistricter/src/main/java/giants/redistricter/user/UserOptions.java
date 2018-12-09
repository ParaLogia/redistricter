package giants.redistricter.user;

import java.io.File;
import java.util.Map;

public class UserOptions {
    Map<String,Boolean> options;

    public void loadOptions(File file){
        return;
    }
    public Boolean get(String option){
        return options.get(option);
    }
    public void set(String option,Boolean value){
        options.put(option,value);
    }
}
