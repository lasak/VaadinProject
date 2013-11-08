package pl.edu.agh.twitter;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtil {

	private static Twitter twitter;

	/**
	 * newest statuses (with biggest date) should be the biggest
	 */
	private static Comparator<Status> statusTimeComparator = new Comparator<Status>() {

		@Override
		public int compare(Status o1, Status o2) {
			if (o1.getCreatedAt().before(o2.getCreatedAt())) {
				return -1;
			} else if (o1.getCreatedAt().equals(o2.getCreatedAt())) {
				return 0;
			} else {
				return 1;
			}

		}
	};

	static {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("xbh7939LQO1TmiTfCIiL1w")
				.setOAuthConsumerSecret(
						"c9wqs3it25ZIL4be9aRFekbp3xR2uptFLVhs10Q67o")
				.setOAuthAccessToken(
						"2180587321-H7DYHIRrlUJEGq9ueZrmUFiN3QInL6QPzm8rlVO")
				.setOAuthAccessTokenSecret(
						"j9agoMg1cQV2r7HT27QEag2COFUo1QAAOZX0NPslzWiyT");
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}

	static public SortedSet<Status> getProperJoinedTimeline() {
		SortedSet<Status> friendTweets = getFriendTweets();
		SortedSet<Status> myTweets = getMyTweets();
		if (friendTweets != null && myTweets != null) {
			SortedSet<Status> allTweets = new TreeSet<Status>(statusTimeComparator);
			allTweets.addAll(friendTweets);
			allTweets.addAll(myTweets);
			return allTweets;
		}
		return null;
	}

	static public SortedSet<Status> getMyTweets() {
		ResponseList<Status> statuses;
		try {
			statuses = twitter.getHomeTimeline();
			SortedSet<Status> sortedStatusSet = new TreeSet<Status>(
					statusTimeComparator);
			for (Status status : statuses) {
				sortedStatusSet.add(status);
			}
			return sortedStatusSet;
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}

	}

	static public SortedSet<Status> getFriendTweets() {
		try {
			List<Long> ids = getFriends();
			SortedSet<Status> sortedStatusSet = new TreeSet<Status>(
					statusTimeComparator);
			for (Long id : ids) {
				ResponseList<Status> statusesList = twitter.getUserTimeline(id);
				for (Status status : statusesList) {
					sortedStatusSet.add(status);
				}
			}
			return sortedStatusSet;
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}

	}

	static private List<Long> getFriends() throws TwitterException {
		long[] l = twitter.getFriendsIDs(-1).getIDs();
		List<Long> friendsIDs = new ArrayList<Long>();
		for (int i = 0; i < l.length; i++) {
			friendsIDs.add(l[i]);
		}
		return friendsIDs;
	}

}
