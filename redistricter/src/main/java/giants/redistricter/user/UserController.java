package giants.redistricter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import giants.redistricter.database.UserService;

@Controller
public class UserController {
    @Autowired UserService userService;
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(path = "/add_user", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        if(userService.addUser(user) == false) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(path = "/verify_login", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody Boolean verifyLogin(@RequestBody User user) {
        return userService.verifyUser(user);
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(path = "/preferences", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody UserPreferences getUserPreferences(@RequestBody User user) {
        return userService.getUserPreferences(user);
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(path = "/preferences/save", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> saveUserPreferences(@RequestBody UserPreferences prefs) {
        if(userService.saveUserPreferences(prefs) == false) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
