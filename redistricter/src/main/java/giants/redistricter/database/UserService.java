package giants.redistricter.database;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import giants.redistricter.user.User;
import giants.redistricter.user.UserPreferences;

@Transactional
@Service
public class UserService {
    private EntityManagerFactory factory = EntityManagerSingleton.getFactory();
    
    public boolean addUser(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        try {
            EntityManager em = factory.createEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            em.close();         
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("User with username "+user.getUsername() + " successfully saved.");
        return true;
    }
    
    public User verifyUser(User user) {
        EntityManager em = factory.createEntityManager();
        User toVerify = em.find(User.class, user.getUsername());
        if(toVerify == null) {
            return null;
        }
        if(BCrypt.checkpw(user.getPassword(), toVerify.getPassword()) == true) {
            return toVerify;
        }
        return null;
    }
    
    public boolean deleteUser(User user) {
        try{
            EntityManager em = factory.createEntityManager();
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
            em.close();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
    public UserPreferences getUserPreferences(User user) {
        try {
            EntityManager em = factory.createEntityManager();
            UserPreferences toReturn = em.find(UserPreferences.class, user.getUsername());
            return toReturn;
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public boolean saveUserPreferences(UserPreferences prefs) {
        try {
            EntityManager em = factory.createEntityManager();
            em.getTransaction().begin();
            em.merge(prefs);
            em.flush();
            em.getTransaction().commit();
            em.close();
            System.out.println("Successfully saved user preferences for user " + prefs.getUsername()+".");
            return true;
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
}
