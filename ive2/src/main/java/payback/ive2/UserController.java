package payback.ive2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String password, Model model, HttpSession session) {
        String message = userService.login(id, password, session); // 세 개의 매개변수를 전달
        if (message.equals("/userMenu") || message.equals("/managerMenu")) {
            session.setAttribute("loggedIn", true);
            session.setAttribute("userId", id); // 사용자 ID를 세션에 저장
            return "redirect:" + message;
        } else {
            model.addAttribute("message", message);
            return "index";
        }
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String id, @RequestParam String password, @RequestParam String name,
            Model model) {
        String message = userService.signup(id, password, name);
        model.addAttribute("message", message);
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/userMenu")
    public String userMenu(HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/";
        }
        return "userMenu";
    }

    @GetMapping("/managerMenu")
    public String managerMenu(HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/";
        }
        return "managerMenu";
    }
}
