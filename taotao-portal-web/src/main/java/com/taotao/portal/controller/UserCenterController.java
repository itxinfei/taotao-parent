package com.taotao.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户中心Controller
 */
@Controller
public class UserCenterController {

    @RequestMapping("/page/user-center")
    public String showUserCenter() {
        return "user-center";
    }
}
