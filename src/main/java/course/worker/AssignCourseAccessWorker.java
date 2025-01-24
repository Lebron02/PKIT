package course.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class AssignCourseAccessWorker {

    private final DataSource dataSource;

    @Autowired
    public AssignCourseAccessWorker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @JobWorker(type = "assignCourseAccess", name = "AssignCourseAccessWorker")
    public void assignCourseAccess(final JobClient client, final ActivatedJob job) {
        System.out.println("Przypisywanie dostępu do kursu...");

        Map<String, Object> variables = job.getVariablesAsMap();
        int userId = (int) variables.get("userId");
        int courseId = (int) variables.get("courseId");
        String courseName = (String) variables.get("chosenCourse");
        String coursePrice = (String) variables.get("finalPrice");

        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println("\nInformacje o zakupionym kursie:");
        System.out.println("Kurs: " + courseName);
        System.out.println("Data i godzina zakupu: " + formattedDateTime);

        // Dodanie kursu do tabeli w bazie danych
        try (Connection connection = dataSource.getConnection()) {
            String insertQuery = "INSERT INTO elearning.user_courses (user_id, course_id, status) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, courseId);
                preparedStatement.setString(3, "zakupiono");
                preparedStatement.executeUpdate();
                System.out.println("Kurs został pomyślnie dodany do bazy danych.");
            }
        } catch (SQLException e) {
            System.err.println("Wystąpił błąd podczas zapisywania kursu w bazie danych: " + e.getMessage());
        }

        System.out.println("Dostęp do kursu został pomyślnie przypisany.");
    }
}