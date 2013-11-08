package pl.edu.agh.twitter;

import java.util.Calendar;
import java.util.Date;
import java.util.SortedSet;

import twitter4j.Status;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ApplicationView extends VerticalLayout implements View{
        
        public static final String APPLICATION_VIEW_NAME = "application";
        private VerticalLayout layout = new VerticalLayout();

        @Override
        public void enter(ViewChangeEvent event) {
                // TODO Auto-generated method stub
                
        }
        
        public ApplicationView() {
                layout.setMargin(true);
                addComponent(layout);

                Button button = new Button("Show tweets from last 4 hours");
                button.addClickListener(new Button.ClickListener() {
                        public void buttonClick(ClickEvent event) {
                                showLast4HoursOfTweets();

                        }
                });
                layout.addComponent(button);
        }
        
        private void showLast4HoursOfTweets() {
                SortedSet<Status> statusesSet = TwitterUtil.getProperJoinedTimeline();
                SortedSet<Status> headSet = statusesSet; // for use within while
                Status last = statusesSet.last();

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR_OF_DAY, -4);
                Date fourHoursAgo = calendar.getTime();

                while (last != null && last.getCreatedAt().after(fourHoursAgo)) {
                        String tweet = last.getUser().getName() + " ("
                                        + last.getCreatedAt().toString() + "):" + last.getText();
                        layout.addComponent(new Label(tweet));
                        headSet = headSet.headSet(last);
                        last = headSet.last();
                }
        }

}