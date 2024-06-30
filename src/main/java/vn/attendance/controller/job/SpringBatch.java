//package vn.travel.controller.job;
//
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.launch.support.SimpleJobLauncher;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.support.IteratorItemReader;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.Scheduled;
//import vn.travel.common.LongZenBatch;
//import vn.travel.util.DataUtil;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class SpringBatch {
//    private static final String BAT_NAME = "springBootBatchZen";
//    private static final String STEP_NAME = "springBootStepZen";
//    public static final Integer CHUCK_SIZE = 30;
//    private final SimpleJobLauncher simpleJobLauncher;
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//    private final ItemWriter writer = new ItemWriter<String>() {
//        @Override
//        public void write(@NonNull List<? extends String> items) throws Exception {
//            if (!DataUtil.isNullOrEmpty(items)) {
//                for (String item : items) {
//                    log.info("==========Data Running : {}", item);
//                }
//            }
//        }
//    };
//
//    private final ItemReader<String> reader = new ItemReader<String>() {
//        private ItemReader<String> delegate;
//
//        @Override
//        public String read() throws Exception {
//            if (Objects.isNull(delegate)) {
//                List<String> data = new ArrayList<>(Arrays.asList("abc", "dcb"));
//                if (!DataUtil.isNullOrEmpty(data)) {
//                    this.delegate = new IteratorItemReader<>(data);
//                }
//            }
//            String read = delegate == null ? null : delegate.read();
//            if (DataUtil.isNullOrEmpty(read)) {
//                delegate = null;
//                return null;
//            }
//            return read;
//        }
//    };
//
//    public Step step() {
//        return stepBuilderFactory
//                .get(STEP_NAME)
//                .chunk(CHUCK_SIZE)
//                .reader(reader)
//                .writer(writer)
//                .build();
//    }
//
//    public Job jobEvent() {
//        return jobBuilderFactory.get(BAT_NAME).incrementer(new RunIdIncrementer()).start(step()).build();
//    }
//
//    @Scheduled(cron = "${spring-boot.batch.cron.test}")
//    @LongZenBatch
//    @SneakyThrows
//    public void scanBatch() {
//        log.info("====================SPRING BOOT BATCH RUNNING===============");
//        JobParameters parameter = new JobParametersBuilder()
//                .addString("JobID", String.valueOf(System.currentTimeMillis()))
//                .toJobParameters();
//        simpleJobLauncher.run(jobEvent(), parameter);
//        log.info("====================SPRING BOOT BATCH STOPPED===============");
//    }
//}
