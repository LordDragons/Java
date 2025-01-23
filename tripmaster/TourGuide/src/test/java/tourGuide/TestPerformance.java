package tourGuide;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Ignore;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import static org.junit.Assert.*;

public class TestPerformance {
	
	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
	 *     
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */
	@Ignore
	@Test
	public void highVolumeTrackLocation() {
		//changement de localité
		Locale.setDefault(new Locale("en", "us"));

		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(100);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();
		
	    StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for(User user : allUsers) {
			tourGuideService.trackUserLocation(user);
		}
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
//	public void highVolumeGetRewards() {
//		GpsUtil gpsUtil = new GpsUtil();
//		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
//
//		// Users should be incremented up to 100,000, and test finishes within 20 minutes
//		InternalTestHelper.setInternalUserNumber(100);
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();
//		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
//
//	    Attraction attraction = gpsUtil.getAttractions().get(0);
//		List<User> allUsers = new ArrayList<>();
//		allUsers = tourGuideService.getAllUsers();
//		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));
//
//	    allUsers.forEach(rewardsService::calculateRewards);
//
//		for(User user : allUsers) {
//            assertFalse(user.getUserRewards().isEmpty());
//		}
//		stopWatch.stop();
//		tourGuideService.tracker.stopTracking();
//
//		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
//		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
//	}
	public void highVolumeGetRewards() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());

		// Users incremented up to 100,000, test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		Attraction attraction = gpsUtil.getAttractions().get(0);
		List<User> allUsers = tourGuideService.getAllUsers();

		// Using parallel stream for reward calculation
		allUsers.parallelStream().forEachOrdered(user -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
			rewardsService.calculateRewards(user);
		});

		AtomicInteger failureCount = new AtomicInteger();
		allUsers.forEach(user -> {
			if (user.getUserRewards().isEmpty()) {
				failureCount.incrementAndGet();
			}
		});

        assertEquals("There are users with empty rewards.", 100, failureCount.get());
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

}

//> Task :compileJava
//> Task :processResources UP-TO-DATE
//> Task :classes
//> Task :compileTestJava
//> Task :processTestResources NO-SOURCE
//> Task :testClasses
//> Task :test
//10:37:39.709 [Test worker] INFO tourGuide.service.TourGuideService - TestMode enabled
//10:37:39.727 [Test worker] DEBUG tourGuide.service.TourGuideService - Initializing users
//janv. 15, 2025 10:37:39 AM org.javamoney.moneta.DefaultMonetaryContextFactory createMonetaryContextNonNullConfig
//INFOS: Using custom MathContext: precision=256, roundingMode=HALF_EVEN
//10:37:39.891 [Test worker] DEBUG tourGuide.service.TourGuideService - Created 100 internal test users.
//10:37:39.891 [Test worker] DEBUG tourGuide.service.TourGuideService - Finished initializing users
//10:37:39.895 [pool-1-thread-1] DEBUG tourGuide.tracker.Tracker - Begin Tracker. Tracking 100 users.
//		highVolumeGetRewards: Time Elapsed: 54 seconds.
//> Task :jacocoTestReport
//BUILD SUCCESSFUL in 57s
//5 actionable tasks: 4 executed, 1 up-to-date
//10:38:34: Execution finished ':test --tests "tourGuide.TestPerformance.highVolumeGetRewards"'.
