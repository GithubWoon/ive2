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
    private final ManagerRepository managerRepository;
    private final ReceiptRepository receiptRepository;  // 이 부분을 추가해주세요.

    public ManagerMenuController(MenuRepository menuRepository, ManagerRepository managerRepository, ReceiptRepository receiptRepository) {  // 이 부분을 수정해주세요.
        this.menuRepository = menuRepository;
        this.managerRepository = managerRepository;
        this.receiptRepository = receiptRepository;  // 이 부분을 추가해주세요.
    }
    @PostMapping("/managerMenu")
    public String handleRequest(@RequestParam String action,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String pw,
            @RequestParam(required = false) String name,
            Model model, HttpServletRequest request) {

        // 메뉴판 보기 클릭시
        if (action.equals("메뉴판 보기")) {
            List<Menu> menus = menuRepository.findAll();
            model.addAttribute("menus", menus);
            return "menu"; 
        }
        // 재고수량 클릭시
        else if (action.equals("재고수량 추가")) {
            if (productName != null && quantity != null && productId != null) {
                Optional<Menu> existingMenu = menuRepository.findByProductName(productName);

                if (existingMenu.isPresent()) {
                    Menu menu = existingMenu.get();
                    menu.setQuantity(menu.getQuantity() + quantity);
                    menuRepository.save(menu);
                } else {
                    Menu newMenu = new Menu();
                    newMenu.setProductName(productName);
                    newMenu.setQuantity(quantity);
                    newMenu.setProductId(productId);
                    menuRepository.save(newMenu);
                }
            }
            return "addQuantity"; 
        }

        // 관리자 추가 클릭시
        else if (action.equals("관리자 추가")) {
            if (id != null && pw != null && name != null) {
                if (managerRepository.existsById(id)) {
                    model.addAttribute("errorMessage", "중복된 ID입니다.");
                    return "addManager";
                }
                Manager newManager = new Manager();
                newManager.setId(id);
                newManager.setPassword(pw);
                newManager.setName(name);
                managerRepository.save(newManager);
                model.addAttribute("successMessage", "관리자가 성공적으로 추가되었습니다."); 
            }
            return "addManager";
        }

        // 구매내역 클릭시
        // else if (action.equals("구매내역 보기")) {
        //     // 구매내역을 가져와서 model에 추가하는 코드가 여기에 위치해야 합니다.
        //     // 현재는 임의의 구매내역을 추가하도록 하겠습니다.
        //     model.addAttribute("purchaseHistory", "임의의 구매내역");
        //     return "Receipt"; // Receipt.html 반환
        // }

        else if (action.equals("구매내역 보기")) {
            // 구매내역을 가져와서 model에 추가하는 코드
            List<Receipt> receipts = receiptRepository.findAll();
            model.addAttribute("purchaseHistory", receipts);
            return "Receipt"; // Receipt.html 반환
        }



        // 종료 클릭시
        else if (action.equals("종료")) {
            request.getSession().invalidate(); 
            return "redirect:/"; 
        } else {
            return "managerMenu"; 
        }
    }
}
