package giants.redistricter.database;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.stereotype.Service;

import giants.redistricter.user.User;

@Service
public class UserService {

    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistence");
    private EntityManager em = factory.createEntityManager();
    
    public boolean addUser(User user) {
        try {
            em.persist(user);  
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("User with username "+user.getUsername() + " successfully saved.");
        return true;
    }
    
}
