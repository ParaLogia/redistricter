package giants.redistricter.database;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerSingleton {
    private final static EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistence");
    
    public static EntityManagerFactory getFactory() {
        return factory;
    }
}
