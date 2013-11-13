package pl.edu.agh.twitter.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class Model {

	private Twitter twitter;
	private static Model model;

	/**
	 * we always want first the newest statuses (with biggest date)
	 */
	private Comparator<Status> statusTimeComparator = new Comparator<Status>() {

		@Override
		public int compare(Status o1, Status o2) {
			if (o1.getCreatedAt().before(o2.getCreatedAt())) {
				return 1;
			} else if (o1.getCreatedAt().equals(o2.getCreatedAt())) {
				return 0;
			} else {
				return -1;
			}

		}
	};

	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("xbh7939LQO1TmiTfCIiL1w")
				.setOAuthConsumerSecret(
						"c9wqs3it25ZIL4be9aRFekbp3xR2uptFLVhs10Q67o")
				.setOAuthAccessToken(
						"2180587321-ODN7s9mZY6VylifdeySxlAYDMLyDm0Ooe4eqDVy")
				.setOAuthAccessTokenSecret(
						"D3gvCYGR0nKWUmSkhJnqvAZu2FjKFTpUJR1CnppQvn2qs");
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}
	
	public static Model getInstance() {
		if (model == null) {
			model = new Model();
		}
		return model;
	}

	public SortedSet<Status> getProperJoinedTimeline() {
		SortedSet<Status> friendTweets = getFriendTweets();
		SortedSet<Status> myTweets = getTweets();
		if (friendTweets != null && myTweets != null) {
			SortedSet<Status> allTweets = new TreeSet<Status>(statusTimeComparator);
			allTweets.addAll(friendTweets);
			allTweets.addAll(myTweets);
			return allTweets;
		}
		return null;
	}

	public SortedSet<Status> getTweets() {
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

	
	// Not used
	public SortedSet<Status> getFriendTweets() {
		try {
			List<Long> ids = getFriendsIds();
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
	
	public List<User> searchUsers(String query) {
		try {
			return twitter.searchUsers(query, 1);
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public User follow(User user) {
		try {
			return twitter.createFriendship(user.getId());
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getFriends() {
		List<User> users= new ArrayList<User>();
		try {
			for (Long id : getFriendsIds()) {
				users.add(twitter.showUser(id));
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	public void sendATweet(String text) {
		try {
			twitter.updateStatus(text);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	private List<Long> getFriendsIds() throws TwitterException {
		long[] l = twitter.getFriendsIDs(-1).getIDs();
		List<Long> friendsIDs = new ArrayList<Long>();
		for (int i = 0; i < l.length; i++) {
			friendsIDs.add(l[i]);
		}
		return friendsIDs;
	}
	


}
