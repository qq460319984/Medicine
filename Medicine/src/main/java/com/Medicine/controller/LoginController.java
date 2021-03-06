package com.Medicine.controller;

import com.Medicine.model.HostHolder;
import com.Medicine.service.UserService;
import com.Medicine.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @RequestMapping(path={"/login"},method = {RequestMethod.GET,RequestMethod.POST})
    public String login(){
        return "login";
    }
    @RequestMapping(path={"/loadin"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String loadin(Model model, @RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam(value="rember", defaultValue = "0") int rememberme,
                         HttpServletResponse response){
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return JSONUtil.getJSONString(0, "成功");
            } else {
                return JSONUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            return JSONUtil.getJSONString(1, "登录异常");
        }
    }
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
