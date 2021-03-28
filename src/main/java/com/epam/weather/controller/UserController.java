package com.epam.weather.controller;

import com.epam.weather.service.UserService;
import com.epam.weather.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping(path="/add")
    public @ResponseBody String addNewUser (@RequestParam String name
            , @RequestParam String email, @RequestParam String password, User user) {
        // @ResponseBody means the returned String is a response, not a view name.
        userService.saveUsers(name, email, password);

        log.info(user.toString()+" saved to the repo");
        return "Saved";
    }

    /**
     * 登陆方法, 用户输入邮箱和密码, 查询数据库检验是否有该账户,如果有,
     * 返回原先页面 ,登陆成功。
     * @param email 用户邮箱
     * @param password 用户密码
     * @param model Spring MVC中的Model，用来储存经过controller处理后的信息，再由View层渲染
     *         得到前端页面。
     * @return
     */
    @GetMapping(path = "/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        List<User> users = userService.findByEmail(email);
        // 如果数据库中未查到该账号:
        if (users == null) {
            log.warn("attempting to log in with the non-existed account");
            return "该用户不存在";
        } else {
            User user = users.get(0);
            if (user.getPassWord().equals(password)) {
                // 如果密码与邮箱配对成功:
                model.addAttribute("name", user.getName());
                log.warn(user.toString()+ " logged in");
            } else {
                // 如果密码与邮箱不匹配:
                model.addAttribute("name", "logging failed");
                log.warn(user.toString()+ " failed to log in");
            }
            return "index";
        }
    }

    /**
     * 查看所有用户的注册信息，按照Spring Boot的设定，以Json的形式输送给用户端。
     * @return
     */
    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    /**
     * 域名的根目录，然后返回的“index”会映射到
     * java/resources/templates/index.html文件。
     * @param namel
     * @return
     */
    @GetMapping(path="/")
    public String welcomePage(@RequestParam(name="name", required=false, defaultValue="World") String namel){
        return "index";
    }


}