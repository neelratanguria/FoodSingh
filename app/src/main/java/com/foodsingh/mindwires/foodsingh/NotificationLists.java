package com.foodsingh.mindwires.foodsingh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanmay on 20-10-2017.
 */

public class NotificationLists {
    private List<NotificationItem> notification = new ArrayList<>();

    public NotificationLists(List<NotificationItem> notificaation){

        notification = notificaation;

    }

    public List<NotificationItem> getNotification(){
        return notification;
    }

}
