package com.richard.cassandra.sstable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created on 11/28/2016.
 */
@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }
}
