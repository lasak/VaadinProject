import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.agh.twitter.model.Model;
import twitter4j.IDs;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

@RunWith(MockitoJUnitRunner.class)
public class ModelTests {
	
	@Mock private Twitter twitter = mock(Twitter.class);
	@InjectMocks Model model = Model.getInstance();

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void getInstanceTest() {
		Model model = Model.getInstance();
		assertEquals(model, Model.getInstance());
	}
	
	@Test
	public void getFriendsEmptyTest() throws TwitterException {
		long[] l = {};
		IDs iDs = mock(IDs.class);
		when(iDs.getIDs()).thenReturn(l);
		when(twitter.getFriendsIDs(anyLong())).thenReturn(iDs);
		assertEquals(0, model.getFriends().size());
	}
	
	@Test
	public void getFriendsStandardTest() throws TwitterException {
		long[] l = {1, 2, 3};
		IDs iDs = mock(IDs.class);
		when(iDs.getIDs()).thenReturn(l);
		when(twitter.getFriendsIDs(anyLong())).thenReturn(iDs);
		when(twitter.showUser(anyLong())).thenReturn(mock(User.class));
		assertEquals(3, model.getFriends().size());
	}
	
	@Test
	public void sendATweetTest() throws TwitterException {
		model.sendATweet("test");
		verify(twitter).updateStatus("test");
	}
	
	@Test
	public void sendATweetExceptionTest() throws TwitterException {
		when(twitter.updateStatus(anyString())).thenThrow(TwitterException.class);
		model.sendATweet("test"); //I expect no exception thrown from within
		verify(twitter).updateStatus("test");
	}
	
	@Test
	public void followTest() throws TwitterException {
		User user = mock(User.class);
		when(user.getId()).thenReturn((long) 3);
		model.follow(user);
		verify(twitter).createFriendship(3);
	}
	
	@Test
	public void followExceptionTest() throws TwitterException {
		when(twitter.createFriendship(anyLong())).thenThrow(TwitterException.class);
		User user = mock(User.class);
		when(user.getId()).thenReturn((long) 3);
		model.follow(user); //I expect no exception thrown from within
		verify(twitter).createFriendship(3);
	}
	
	@Test
	public void searchUsersTest() throws TwitterException {
		ResponseList list = mock(ResponseList.class);
		when(twitter.searchUsers(anyString(), anyInt())).thenReturn(list);
		List list2 = model.searchUsers("test");
		verify(twitter).searchUsers(anyString(), anyInt());
		assertEquals(list, list2);
	}
	
	@Test
	public void searchUsersExceptionTest() throws TwitterException {
		when(twitter.searchUsers(anyString(), anyInt())).thenThrow(TwitterException.class);
		List list2 = model.searchUsers("test"); //I expect no exception thrown from within
		verify(twitter).searchUsers(anyString(), anyInt());
		assertEquals(null, list2);
	}

}
