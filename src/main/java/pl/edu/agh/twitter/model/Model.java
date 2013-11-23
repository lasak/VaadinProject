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

/**
 * 
 * Model class responsible for connecting to Twitter and communication with it.
 * 
 */
public class Model {

	private Twitter twitter;
	private static Model model;

	// we always want first the newest statuses (with biggest date)
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

	private Model() {
	}

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

	/**
	 * Returns the single instance of {@code Model}
	 * 
	 * @return instance of model
	 */
	public static Model getInstance() {
		if (model == null) {
			model = new Model();
		}
		return model;
	}

	/**
	 * Gets the time line from twitter. It consists of user tweets and followed
	 * people tweets
	 * 
	 * @return sorted tweets
	 */
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

	/**
	 * Sends a search users request to Twitter
	 * 
	 * @param text
	 *            to be matched
	 * @return found list of users
	 */
	public List<User> searchUsers(String text) {
		try {
			return twitter.searchUsers(text, 1);
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Sends a follow request to Twitter
	 * 
	 * @param user
	 *            to be followed
	 * @return new followed user
	 */
	public User follow(User user) {
		try {
			return twitter.createFriendship(user.getId());
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets list of followed people
	 * 
	 * @return list of followed people
	 */
	public List<User> getFriends() {
		List<User> users = new ArrayList<User>();
		try {
			for (Long id : getFriendsIds()) {
				users.add(twitter.showUser(id));
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return users;
	}

	/**
	 * Sends a tweet request to Twitter
	 * 
	 * @param text
	 *            tweet message
	 */
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
