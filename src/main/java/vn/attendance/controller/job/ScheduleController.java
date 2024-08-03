package vn.attendance.controller.job;//package vn.sphinx.hysmart.logistic.controller.job;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
//import org.springframework.scheduling.config.ScheduledTask;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import vn.sphinx.hysmart.logistic.syncJob.toInternal.JobKafka;
//import vn.sphinx.hysmart.logistic.syncJob.toInternal.SyncYcDbTrongNgoaiToInternal;
//
//import java.util.Set;
//
///**
// * Cho phep control duoc cac annotation Schedule trong springboot nhu start va stop job
// */
//@RestController
//@RequestMapping("schedule")
//public class ScheduleController {
//    private static final String SCHEDULE_TASK = "Schedule Tasks";
//    @Autowired
//    ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor;
//    @Autowired
//    JobKafka jobKafka;
//    @Autowired
//    SyncYcDbTrongNgoaiToInternal syncYcDbTrongNgoaiToInternal;
//    @GetMapping("start-jobkafka")
//    public String startKafkaJob(){
//        scheduledAnnotationBeanPostProcessor.postProcessAfterInitialization(jobKafka,SCHEDULE_TASK);
//        return "OK";
//    }
//    @GetMapping("stop-jobkafka")
//    public String stopKafkaJob(){
//        scheduledAnnotationBeanPostProcessor.postProcessBeforeDestruction(jobKafka,SCHEDULE_TASK);
//        return "OK";
//    }
//    @GetMapping("stop-sync")
//    public String stopSyncJob(){
//        scheduledAnnotationBeanPostProcessor.postProcessBeforeDestruction(syncYcDbTrongNgoaiToInternal,SCHEDULE_TASK);
//        return "OK";
//    }
//    @GetMapping(value = "/list")
//    public String listSchedules() throws JsonProcessingException {
//        Set<ScheduledTask> setTasks = scheduledAnnotationBeanPostProcessor.getScheduledTasks();
//        if (!setTasks.isEmpty()) {
//            return setTasks.toString();
//        } else {
//            return "Currently no scheduler tasks are running";
//        }
//    }
//
//}
