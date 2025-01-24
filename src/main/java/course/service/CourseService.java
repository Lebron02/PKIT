package course.service;

import course.model.Course;
import course.repository.CourseRepository;
import course.repository.UserCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, UserCourseRepository userCourseRepository) {
        this.courseRepository = courseRepository;
        this.userCourseRepository = userCourseRepository;
    }

    /**
     * Pobierz wszystkie kursy dostępne w systemie.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Pobierz kursy, których użytkownik nie posiada (status != 'zakupiono').
     */
    public List<Course> getCoursesNotOwnedByUser(int userId) {
        return userCourseRepository.findCoursesNotOwnedByUser(userId);
    }

    /**
     * Pobierz cenę kursu na podstawie jego ID.
     */
    public Double getCoursePriceById(int courseId) {
        Double price = courseRepository.findPriceByCourseId(courseId);
        if (price == null) {
            throw new IllegalArgumentException("Nie znaleziono kursu o ID: " + courseId);
        }
        return price;
    }
}