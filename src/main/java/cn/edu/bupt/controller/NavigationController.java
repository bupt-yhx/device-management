package cn.edu.bupt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/12/23.
 *
 * 返回前端请求的html,html静态资源主要放在resouce/template下面
 */
@Controller
@Slf4j
public class NavigationController {

    @Autowired
    private HttpServletRequest request;
    
    @RequestMapping("/")
    public String  index() {
        return "index";
    }

    @RequestMapping("/signin")
    public String  login() {
        return "signin";
    }

    @RequestMapping("/signup")
    public String  logout() {
        return "signup";
    }

    @RequestMapping("/device_group")
    public String  device_group() {
        return "device_group";
    }

    @RequestMapping("/homepages")
    public String  homepages() {
        return "homepages";
    }

    @RequestMapping("/homepage")
    public String  homepage() {
        return "device";
    }

    @RequestMapping("/services")
    public String  services() {
        return "services";
    }

    @RequestMapping("/rules")
    public String  rules() {
        return "rules";
    }

    @RequestMapping("/plugins")
    public String  plugins() { return "plugins"; }

    @RequestMapping("/assess")
    public String  assess() {
        return "assess";
    }

}
