package course.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CheckUserDataWorker {

    @JobWorker(type = "checkUserData", name = "checkUserDataWorker")
    public Map<String, Object> checkUserData(final JobClient client, final ActivatedJob job) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("dataValid", true);
        return variables;
    }
}
