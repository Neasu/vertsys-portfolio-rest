/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw.webservices;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Marius
 */

@RestController
public class SearchService {

    @RequestMapping("/search")
    public String search(@RequestParam(value = "query") String query, @RequestParam(value = "type") String type){

        return "";
    }


}
