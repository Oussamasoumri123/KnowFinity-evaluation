package nest.esprit.userserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class CONTROLLER {
    @GetMapping("/sayHello")
    public String sayHello() {
        return "Hello from user server";
    }
}
