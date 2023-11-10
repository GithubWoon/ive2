package payback.ive2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.sql.Timestamp;

@Controller
public class UserMenuController {
    private final MenuRepository menuRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public UserMenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;

    }

    @PostMapping("/userMenu")
    public String handleRequest(@RequestParam String action, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(); // 세션 객체 얻기
        String userId = (String) session.getAttribute("userId");

        if (action.equals("메뉴판 보기")) {
            return handleMenuView(model, session, userId);
        } else if (action.equals("종료")) {
            return handleExit(request);
        } else if (action.equals("장바구니 담기")) {
            return handleBasketAdd(model);
        } else if (action.equals("장바구니 보기")) {
            return handleBasketView(model, userId);
        } else if (action.equals("잔액 충전")) {
            return "charge";
        } else {
            return "userMenu"; // 그밖의 action은 userMenu.html로 다시 돌아감
        }
    }

    // 메뉴판 보여줌
    private String handleMenuView(Model model, HttpSession session, String userId) {
        List<Menu> menus = menuRepository.findAll();
        model.addAttribute("menus", menus);
        updateBalance(session, userId);
        return "menu"; // menu.html 반환
    }

    // 잔액 추가
    private void updateBalance(HttpSession session, String userId) {
        try {
            String sql = "SELECT BALANCE FROM BALANCE WHERE ID = ?";
            Integer balance = jdbcTemplate.queryForObject(sql, Integer.class, userId);
            if (balance == null) {
                balance = 0;
            }
            session.setAttribute("balance", balance);
        } catch (EmptyResultDataAccessException e) {
            // 결과가 없으면 기본 잔액을 0으로 설정
            session.setAttribute("balance", 0);
        }
    }

    // 초기 페이지로 돌아가줌
    private String handleExit(HttpServletRequest request) {
        request.getSession().invalidate(); // 세션 초기화
        return "redirect:/"; // 초기 페이지로 리다이렉트
    }

    // 장바구니 추가
    private String handleBasketAdd(Model model) {
        List<Menu> menus = menuRepository.findAll();
        model.addAttribute("menus", menus);
        return "basket";
    }

    // 장바구니 보여줌
    private String handleBasketView(Model model, String userId) {
        // userId와 일치하는 BASKET 테이블의 데이터를 가져옴
        String sql = "SELECT PRODUCTNAME, QUANTITY FROM BASKET WHERE ID = ?";
        List<Map<String, Object>> basketItems = jdbcTemplate.queryForList(sql, userId);

        // 가져온 데이터를 모델에 추가
        model.addAttribute("basketItems", basketItems);

        return "look";
    }

    @PostMapping("/addToBasket")
    public ResponseEntity<Map<String, String>> addToBasket(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");
        String userId = (String) session.getAttribute("userId");
        Enumeration<String> parameterNames = request.getParameterNames();

        // HashMap 에 메세지를 담을건가보다
        Map<String, String> response = new HashMap<>();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();

            if (paramName.startsWith("productName")) {
                String productName = request.getParameter(paramName);
                String quantity = request.getParameter("quantity" + paramName.substring(11));
                int orderQuantity = Integer.parseInt(quantity);

                Optional<Menu> optionalMenu = menuRepository.findByProductName(productName);

                Menu menu = optionalMenu.get();

                if (orderQuantity < 1 || orderQuantity > menu.getQuantity()) {
                    response.put("message", "주문 수량 제대로 입력했어?");
                    return ResponseEntity.ok(response);
                }

                String sqlCheck = "SELECT * FROM BASKET WHERE ID = ? AND PRODUCTNAME = ?";
                List<Map<String, Object>> matchingItems = jdbcTemplate.queryForList(sqlCheck, userId, productName);

                if (!matchingItems.isEmpty()) {
                    String sqlUpdate = "UPDATE BASKET SET QUANTITY = QUANTITY + ? WHERE ID = ? AND PRODUCTNAME = ?";
                    jdbcTemplate.update(sqlUpdate, orderQuantity, userId, productName);
                } else {
                    String sqlInsert = "INSERT INTO BASKET (ID, NAME, PRODUCTNAME, QUANTITY) VALUES (?, ?, ?, ?)";
                    jdbcTemplate.update(sqlInsert, userId, userName, productName, orderQuantity);
                }
            }
        }

        response.put("message", "추가완료 한번 확인해봐");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/look")
    public ResponseEntity<Map<String, String>> handleLookRequest(@RequestParam String action, Model model,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");

        Map<String, String> response = new HashMap<>();

        // 취소 클릭시
        if (action.equals("취소")) {
            // 로그인한 사용자의 BASKET 테이블의 모든 데이터 삭제
            String sql = "DELETE FROM BASKET WHERE ID = ?";
            jdbcTemplate.update(sql, userId);

            response.put("message", "장바구니를 초기화하였습니다.");
            return ResponseEntity.ok(response);
        }

        // 구매 클릭시
        else if (action.equals("구매")) {
            // 로그인한 사용자의 BASKET 테이블의 모든 데이터를 MENU 테이블에서 감소
            String sqlSelect = "SELECT PRODUCTNAME, QUANTITY FROM BASKET WHERE ID = ?";
            List<Map<String, Object>> basketItems = jdbcTemplate.queryForList(sqlSelect, userId);

            // 현재 시간을 가져옴
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            // RECEIPT 테이블에서 현재 PURCHASENUM의 최댓값을 찾음
            String sqlMaxPurchaseNum = "SELECT MAX(PURCHASENUM) FROM RECEIPT";
            Integer maxPurchaseNum = jdbcTemplate.queryForObject(sqlMaxPurchaseNum, Integer.class);

            // 최댓값이 null인 경우(테이블에 데이터가 없는 경우) 처리
            int purchaseNum = (maxPurchaseNum == null) ? 1 : maxPurchaseNum + 1;

            for (Map<String, Object> item : basketItems) {
                String productName = (String) item.get("PRODUCTNAME");
                int quantity = ((BigDecimal) item.get("QUANTITY")).intValue();

                // RECEIPT 테이블에 데이터 추가
                String sqlInsert = "INSERT INTO RECEIPT (ID, NAME, PRODUCTNAME, QUANTITY, BUYDAY, PURCHASENUM) VALUES (?, ?, ?, ?, ?, ?)";
                jdbcTemplate.update(sqlInsert, userId, userName, productName, quantity, timestamp, purchaseNum);

                String sqlUpdate = "UPDATE MENU SET QUANTITY = QUANTITY - ? WHERE PRODUCTNAME = ?";
                jdbcTemplate.update(sqlUpdate, quantity, productName);
            }

            // 로그인한 사용자의 BASKET 테이블의 모든 데이터 삭제
            String sqlDelete = "DELETE FROM BASKET WHERE ID = ?";
            jdbcTemplate.update(sqlDelete, userId);

            response.put("message", "구매를 완료하였습니다.");
            return ResponseEntity.ok(response);
        }

        else {
            response.put("message", "알 수 없는 요청입니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // UserMenuController.java
    @PostMapping("/charge")
    public String handleCharge(@RequestParam Integer amount, HttpServletRequest request) {
        HttpSession session = request.getSession(); // 세션 객체 얻기
        String userId = (String) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");

        // BALANCE 테이블에서 ID와 NAME으로 잔액을 찾아 업데이트
        String sql = "UPDATE BALANCE SET BALANCE = BALANCE + ? WHERE ID = ? AND NAME = ?";
        jdbcTemplate.update(sql, amount, userId, userName);

        return "redirect:/userMenu"; // 충전 후 메뉴 페이지로 리다이렉트
    }

    

}
