package course.repository;

import course.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {

    // Pobierz wszystkie moduły dla danego kursu, posortowane rosnąco po orderNum
    List<Module> findByCourseCourseIdOrderByOrderNumAsc(int courseId);
}