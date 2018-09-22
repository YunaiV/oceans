package cn.iocoder.webapp.bff.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/info")
    public String info() {
        // TODO 芋艿，正在实现中
//        return String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return null;
    }

}