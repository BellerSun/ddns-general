package cn.sunyc.ddnsgeneral.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author sun yu chao
 * @version 1.0
 * @since 2023/1/18 16:18
 */
@Controller
@RequestMapping("/html")
public class PageController {


    @RequestMapping("/index")
    public String index() {
        return "index";
    }


    @RequestMapping("/**")
    public String all() {
        return "index";
    }
}
