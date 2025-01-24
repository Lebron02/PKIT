package course.worker;

import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class ConfirmPayment {

    @JobWorker(type = "paymentConfirmation", name = "paymentConfirmationWorker")
    public Map<String, Object> paymentConfirmation(final JobClient client, final ActivatedJob job) {
        System.out.println("\nPotwierdź płatność klikając [y]");

        // Pobranie zmiennych procesu
        Map<String, Object> vars = job.getVariablesAsMap();

        boolean paymentValid = false;

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (true) {
            if (input.equals("y")) {
                paymentValid = true;
                System.out.println("Płatność została potwierdzona.");
                break;
            } else {
                System.out.println("Nieprawidłowe dane. Spróbuj ponownie:");
                input = scanner.nextLine();
            }
        }

        Map<String, Object> outputVars = new HashMap<>(vars);
        outputVars.put("paymentValid", paymentValid);
        return outputVars;
    }
}
