package giants.redistricter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import giants.redistricter.data.State;
import giants.redistricter.data.StateLoaderService;

@SpringBootApplication
public class RedistricterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedistricterApplication.class, args);
//        StateLoaderService serv = new StateLoaderService();
//        State state = serv.getStateByShortName("NY");
//        System.out.println(state.getPopulation());
    }
}
