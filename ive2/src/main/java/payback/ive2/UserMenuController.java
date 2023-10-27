package payback.ive2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserMenuController {
    private final MenuRepository menuRepository;

    public UserMenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @PostMapping("/userMenu")
    public String handleRequest(@RequestParam String action, Model model, HttpServletRequest request) {
        // 메뉴판 보기 클릭시
        if (action.equals("메뉴판 보기")) {
            List<Menu> menus = menuRepository.findAll();
            model.addAttribute("menus", menus);
            return "menu"; // menu.html 반환

            // 종료 클릭시
        } else if (action.equals("종료")) {
            request.getSession().invalidate(); // 세션 초기화
            return "redirect:/"; // 초기 페이지로 리다이렉트

        } else if (action.equals("장바구니 담기")) {
            List<Menu> menus = menuRepository.findAll();
            model.addAttribute("menus", menus);
            // 장바구니 담기 로직 구현
            return "basket";

        } else {
            return "userMenu"; // 그밖의 action은 userMenu.html로 다시 돌아감
        }
    }

}
