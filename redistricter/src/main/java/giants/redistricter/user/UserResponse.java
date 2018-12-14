package giants.redistricter.user;

public class UserResponse {
    private Boolean isValid;
    private Boolean isAdmin;
    
    public UserResponse() {
        
    }
    
    public UserResponse(Boolean isValid, Boolean isAdmin) {
        this.isAdmin= isAdmin;
        this.isValid = isValid;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    
}
