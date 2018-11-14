package giants.redistricter.user;

public class AdminService {
    String username;
    Boolean adminLoggedIn;
//    EntityManagerFactory emf;
//    EntityManager em;

    public Boolean isValidAdmin(String username, String password){
        return false;
    }

    public Boolean login(String username, String password){
        return false;
    }
    public Boolean logout(){
        return false;
    }

    public void registerAdmin(String username, String password){
        return;
    }

    public void deregisterAdmin(String username){
        return;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAdminLoggedIn() {
        return adminLoggedIn;
    }

    public void setAdminLoggedIn(Boolean adminLoggedIn) {
        this.adminLoggedIn = adminLoggedIn;
    }

//    public EntityManagerFactory getEmf() {
//        return emf;
//    }
//
//    public void setEmf(EntityManagerFactory emf) {
//        this.emf = emf;
//    }
//
//    public EntityManager getEm() {
//        return em;
//    }
//
//    public void setEm(EntityManager em) {
//        this.em = em;
//    }
}
