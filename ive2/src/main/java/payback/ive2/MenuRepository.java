package payback.ive2;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByProductName(String productName);
}
