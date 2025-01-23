//package com.eservice.ms_tracker.service;
//
//import com.eservice.ms_tracker.consumer.GuideGateway;
//import com.eservice.ms_user.service.UserService;
//import org.apache.commons.lang3.time.StopWatch;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class TrackerService implements Runnable {
//    private final Logger logger = LoggerFactory.getLogger(TrackerService.class);
//    private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
//    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
//    private final GuideGateway guideGateway;
//    private static boolean stop = false;
//
//    public TrackerService(GuideGateway guideGateway) {
//        this.guideGateway = guideGateway;
//        executorService.submit(this);
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
//        while (true) {
//            if (Thread.currentThread().isInterrupted() || stop) {
//                logger.debug("Tracker stopping");
//                break;
//            }
//
//            List<UserService> users = guideGateway.getAllUsers();  // Fetch users from ms-guide
//            logger.debug("Begin Tracker. Tracking {} users.", users.size());
//
//            stopWatch.start();
//            users.parallelStream().forEach(guideGateway::trackUserLocation);  // Track user location via ms-guide
//            stopWatch.stop();
//            logger.debug("Tracker Time Elapsed: {} seconds.", TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
//            stopWatch.reset();
//
//            try {
//                logger.debug("Tracker sleeping");
//                TimeUnit.SECONDS.sleep(trackingPollingInterval);
//            } catch (InterruptedException e) {
//                break;
//            }
//        }
//    }
//}
