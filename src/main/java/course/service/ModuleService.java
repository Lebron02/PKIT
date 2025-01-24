package course.service;

import course.model.Module;
import course.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    @Autowired
    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public List<Module> getModulesByCourseId(int courseId) {
        return moduleRepository.findByCourseCourseIdOrderByOrderNumAsc(courseId);
    }
}