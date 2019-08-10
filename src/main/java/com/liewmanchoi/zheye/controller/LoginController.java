package com.liewmanchoi.zheye.controller;

import com.liewmanchoi.zheye.service.UserService;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wangsheng
 * @date 2019/5/16
 */
@Controller
@Slf4j
public class LoginController {
  @Autowired UserService userService;

  @RequestMapping(path = "/reg/", method = RequestMethod.POST)
  public String register(
      Model model,
      @RequestParam("username") String username,
      @RequestParam("password") String password,
      @RequestParam("next") String next,
      @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
      HttpServletResponse response) {
    try {
      Map<String, Object> map = userService.register(username, password);
      final String ticket = "ticket";
      if (map.containsKey(ticket)) {
        Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
        cookie.setPath("/");

        if (rememberme) {
          cookie.setMaxAge(3600 * 24 * 5);
        }

        response.addCookie(cookie);
        if (StringUtils.isNotBlank(next)) {
          return "redirect:" + next;
        }
        return "redirect:/";
      } else {
        model.addAttribute("msg", map.get("msg"));
        return "login";
      }
    } catch (Exception exception) {
      log.error("注册异常 " + exception.getMessage());
      model.addAttribute("msg", "服务器错误");
      return "login";
    }
  }

  @RequestMapping(path = "/login/", method = RequestMethod.POST)
  public String login(
      Model model,
      @RequestParam("username") String username,
      @RequestParam("password") String password,
      @RequestParam(value = "next", required = false) String next,
      @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
      HttpServletResponse response) {
    try {
      Map<String, Object> map = userService.login(username, password);
      final String ticket = "ticket";
      if (map.containsKey(ticket)) {
        Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
        cookie.setPath("/");

        if (rememberme) {
          cookie.setMaxAge(3600 * 24 * 5);
        }

        response.addCookie(cookie);
        if (!StringUtils.isBlank(next)) {
          return "redirect:" + next;
        }
        return "redirect:/";
      } else {
        model.addAttribute("msg", map.get("msg"));
        return "login";
      }
    } catch (Exception exception) {
      log.error("注册异常 " + exception.getMessage());
      model.addAttribute("msg", "服务器错误");
      return "login";
    }
  }

  @RequestMapping(path = "/reglogin", method = RequestMethod.GET)
  public String reLogin(Model model, @RequestParam(value = "next", required = false) String next) {
    model.addAttribute("next", next);
    return "login";
  }

  @RequestMapping(
      path = "/logout",
      method = {RequestMethod.GET, RequestMethod.POST})
  public String logout(@CookieValue("ticket") String ticket) {
    userService.logout(ticket);
    return "redirect:/";
  }
}
