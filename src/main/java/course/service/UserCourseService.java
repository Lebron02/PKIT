package course.service;

import course.model.Course;
import course.model.User;
import course.repository.UserCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import course.repository.CourseRepository;

import java.util.List;

@Service
public class UserCourseService {

    private final UserCourseRepository userCourseRepository;
    private final AuthService authService;
    private final CourseRepository courseRepository;

    @Autowired
    public UserCourseService(UserCourseRepository userCourseRepository, AuthService authService, CourseRepository courseRepository) {
        this.userCourseRepository = userCourseRepository;
        this.authService = authService;
        this.courseRepository = courseRepository;
    }

    /**
     * Kursy, które użytkownik już zakupił (status = 'zakupiono').
     */
    public List<Course> getOwnedCourses(int userId) {
        return userCourseRepository.findCoursesByUserId(userId);
    }

    /**
     * Pobierz dane użytkownika i kursu na podstawie userId i courseId.
     */
    public String getUserAndCourseInfo(int userId, int courseId) {
        // Pobierz użytkownika
        User user = authService.getUserById(userId);

        // Pobierz kurs
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono kursu o ID = " + courseId));

        // Formatowanie wyniku
        return String.format("Imię: %s, Nazwisko: %s, Kurs: %s",
                user.getFirstName(), user.getLastName(), course.getCourseName());
    }
}