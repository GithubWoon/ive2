package payback.ive2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import payback.ive2.Receipt;
import payback.ive2.ReceiptRepository;

@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Receipt saveReceipt(Receipt receipt) {
        return receiptRepository.save(receipt);
    }
}