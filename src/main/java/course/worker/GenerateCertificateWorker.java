package course.worker;

import course.service.UserCourseService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class GenerateCertificateWorker {

    private final UserCourseService userCourseService;

    @Autowired
    public GenerateCertificateWorker(UserCourseService userCourseService) {
        this.userCourseService = userCourseService;
    }

    @JobWorker(type = "generateCertificate", name = "GenerateCertificateWorker")
    public Map<String, String> generateCertificate(final JobClient client, final ActivatedJob job) {
        System.out.println("Rozpoczynam generowanie certyfikatu...");

        Map<String, Object> variables = job.getVariablesAsMap();
        int userId = (int) variables.get("userId");
        int courseId = (int) variables.get("courseId");

        String userAndCourseInfo = userCourseService.getUserAndCourseInfo(userId, courseId);

        String[] userDetails = userAndCourseInfo.split(", ");
        String firstName = userDetails[0].split(": ")[1];
        String lastName = userDetails[1].split(": ")[1];
        String courseName = userDetails[2].split(": ")[1];

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Generowanie treści certyfikatu
        String certificate = String.format(
                "CERTYFIKAT UKOŃCZENIA KURSU\n\n" +
                        "Imię i nazwisko: %s %s\n" +
                        "Nazwa kursu: %s\n" +
                        "Data wygenerowania: %s\n\n" +
                        "Gratulujemy ukończenia kursu!",
                firstName, lastName, courseName, currentDate
        );

        System.out.println("\nWygenerowany certyfikat:\n");
        System.out.println(certificate);


        return Map.of("certificate", certificate);
    }
}