package com.eservice.ms_tracker.service;


//import com.eservice.ms_tracker.consumer.TrackerGateway;
//import com.eservice.ms_tracker.consumer.UserGateway;
//import com.eservice.ms_user.model.User;
//import org.apache.commons.lang3.time.StopWatch;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class TrackerService implements Runnable {
//    private static final Logger logger = LoggerFactory.getLogger(TrackerService.class);
//    private static final long TRACKING_INTERVAL = TimeUnit.MINUTES.toSeconds(5);
//    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
//    private final UserGateway userGateway;
//    private static boolean stop = false;
//    private final TrackerGateway trackerGateway;
//
//    @Autowired
//    public TrackerService(UserGateway userGateway, TrackerGateway trackerGateway) {
//        this.userGateway = userGateway;
//        executorService.submit(this);
//        this.trackerGateway = trackerGateway;
//    }
//
//    public static void stopTracking() {
//        stop = true;
//        executorService.shutdownNow();
//    }
//
//    @Override
//    public void run() {
//        StopWatch stopWatch = new StopWatch();
//        while (!stop) {
//            if (Thread.currentThread().isInterrupted()) {
//                logger.info("Tracker interrupted. Stopping.");
//                break;
//            }
//            try {
//                List<User> users = trackerGateway.getAllUsersFromTrackerGateway();
//                logger.info("Tracking {} users.", users.size());
//
//                stopWatch.reset();
//                stopWatch.start();
//
//                users.parallelStream().forEach(userGateway::trackUserLocation);
//
//                stopWatch.stop();
//                logger.info("Tracking completed in {} seconds.", stopWatch.getTime(TimeUnit.SECONDS));
//
//                TimeUnit.SECONDS.sleep(TRACKING_INTERVAL);
//            } catch (InterruptedException e) {
//                logger.warn("Tracker interrupted during sleep.");
//                Thread.currentThread().interrupt();
//                break;
//            } catch (Exception e) {
//                logger.error("Error during tracking process: ", e);
//            }
//        }
//        executorService.shutdown();
//    }
//}

