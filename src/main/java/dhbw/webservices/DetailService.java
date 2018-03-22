package dhbw.webservices;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DetailService {

    @RequestMapping("/detail/{id}")
    public void detail(){

    }
}
