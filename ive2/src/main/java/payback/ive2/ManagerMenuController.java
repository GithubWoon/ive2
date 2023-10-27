package payback.ive2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ManagerMenuController {
    private final MenuRepository menuRepository;

    public ManagerMenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @PostMapping("/managerMenu")
    // /manager 에서 어떤 action 을 선택하냐에 따라 작동되는 로직이 다름
    public String handleRequest(@RequestParam String action,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) Long productId,
            Model model, HttpServletRequest request) {

        // 메뉴판 보기 클릭시
        if (action.equals("메뉴판 보기")) {
            List<Menu> menus = menuRepository.findAll();
            model.addAttribute("menus", menus);
            return "menu"; // menu.html 반환
        
        // 재고수량 클릭시
        } else if (action.equals("재고수량 추가")) {
            if (productName != null && quantity != null && productId != null) {
                Optional<Menu> existingMenu = menuRepository.findByProductName(productName);

                if (existingMenu.isPresent()) {
                    // 이미 존재하는 메뉴의 경우, 수량만 업데이트
                    Menu menu = existingMenu.get();
                    menu.setQuantity(menu.getQuantity() + quantity);
                    menuRepository.save(menu);
                } else {
                    // 새로운 메뉴의 경우, 메뉴 추가
                    Menu newMenu = new Menu();
                    newMenu.setProductName(productName);
                    newMenu.setQuantity(quantity);
                    newMenu.setProductId(productId);
                    menuRepository.save(newMenu);
                }
            }
            return "addQuantity"; // addQuantity.html 반환

        // 종료 클릭시
        } else if (action.equals("종료")) {
            request.getSession().invalidate(); // 세션 초기화
            return "redirect:/"; // 초기 페이지로 리다이렉트

        } else {
            return "managerMenu"; // 그밖의 action은 managerMenu.html, 구매내역 보기, 관리자 추가 로직 필요함
        }
    }
}
