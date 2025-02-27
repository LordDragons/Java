package tourGuide.tracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourGuide.service.TourGuideService;
import tourGuide.user.User;

public class Tracker extends Thread {
	private final Logger logger = LoggerFactory.getLogger(Tracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;
	private boolean stop = false;

	public Tracker(TourGuideService tourGuideService) {
		this.tourGuideService = tourGuideService;
		
		executorService.submit(this);
	}
	
	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}
	
	@Override
//	public void run() {
//		StopWatch stopWatch = new StopWatch();
//		while(true) {
//			if(Thread.currentThread().isInterrupted() || stop) {
//				logger.debug("Tracker stopping");
//				break;
//			}
//
//			List<User> users = tourGuideService.getAllUsers();
//            logger.debug("Begin Tracker. Tracking {} users.", users.size());
//			stopWatch.start();
//			users.forEach(tourGuideService::trackUserLocation);
//			stopWatch.stop();
//            logger.debug("Tracker Time Elapsed: {} seconds.", TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
//			stopWatch.reset();
//			try {
//				logger.debug("Tracker sleeping");
//				TimeUnit.SECONDS.sleep(trackingPollingInterval);
//			} catch (InterruptedException e) {
//				break;
//			}
//		}
//
//	}
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while (true) {
			if (Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}

			List<User> users = tourGuideService.getAllUsers();  // Fetch users once and reuse
			logger.debug("Begin Tracker. Tracking {} users.", users.size());

			stopWatch.start();
			users.parallelStream().forEach(tourGuideService::trackUserLocation);  // Parallel tracking
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: {} seconds.", TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
			stopWatch.reset();

			try {
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
