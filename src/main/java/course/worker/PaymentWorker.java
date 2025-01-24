package course.worker;

import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class PaymentWorker {
    @JobWorker(type = "payment", name = "paymentWorker")
    public void processPayment(final JobClient client, final ActivatedJob job) {
        System.out.println("\nRozpoczęto inicjalizację płatności...");

        Map<String, Object> vars = job.getVariablesAsMap();

        Object originalPriceObj = vars.get("OriginalPrice");
        if (originalPriceObj == null) {
            throw new IllegalArgumentException("Brak zmiennej OriginalPrice. Proces nie może kontynuować.");
        }

        BigDecimal originalPrice = new BigDecimal(originalPriceObj.toString());

        Object discountValueObj = vars.get("getDiscounts");
        BigDecimal finalPrice;

        if (discountValueObj != null && discountValueObj instanceof Number) {
            double discountValue = ((Number) discountValueObj).doubleValue();
            System.out.println("Zniżka wynosi: " + discountValue + "%");

            BigDecimal discountFactor = BigDecimal.valueOf(1 - (discountValue / 100));
            finalPrice = originalPrice.multiply(discountFactor).setScale(2, BigDecimal.ROUND_HALF_UP);
            System.out.println("Cena końcowa po uwzględnieniu zniżki: " + finalPrice + " zł");
        } else {
            System.out.println("Brak zniżki. Cena końcowa: " + originalPrice + " zł");
            finalPrice = originalPrice;
        }

        String paymentId = UUID.randomUUID().toString();

        Map<String, Object> outputVars = new HashMap<>();
        outputVars.put("paymentId", paymentId);
        outputVars.put("FinalPrice", finalPrice);


        System.out.println("\npaymentId: " + paymentId);
    }
}
