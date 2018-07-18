package com.foodsingh.mindwires.foodsingh;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Soumya Deb on 18-10-2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    Bitmap h;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);



        Map<String,String> map = remoteMessage.getData();



        String img = map.get("icon");
        String title = map.get("title");
        String body = map.get("body");
        String sound = map.get("sound");
        String activity = map.get("activity");
        String notification = map.get("notification");
        String url = map.get("url");
        String coupon = map.get("coupon");


        if(img.length()!=0&&title.length()!=0&&body.length()!=0&&sound.length()!=0
                &&activity.length()!=0&&notification.length()!=0&&url.length()!=0&&coupon.length()!=0){
            try {
                h = getBitmapFromURL(img,remoteMessage);

                Log.i("notification","("+body.length()+")");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("notificationException",e.toString());
                sendNotification2(remoteMessage.getData(),remoteMessage,null);
                Log.i("android_notification3","null notification received.");
            }finally {
             //   sendNotification2(remoteMessage.getData(),remoteMessage,h);
            }
        }else{
            sendNotification2(remoteMessage.getData(),remoteMessage,null);
            Log.i("android_notification4","null notification received.");
        }
    }

    public Bitmap getBitmapFromURL(String strURL, RemoteMessage remoteMessage) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            sendNotification2(remoteMessage.getData(),remoteMessage,myBitmap);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("android_notification--","null notification received.");
            return null;
        }
    }

    private NotificationItem getNotificationItem(Map<String,String> map,long time){
        String title=map.get("title");
        String body = map.get("body");

        String notificationType = map.get("notification");

        String activity = map.get("activity");

        String img = map.get("icon");

        String url = map.get("url");

        return new NotificationItem(body,title,img,url,activity, notificationType,time+"",false, map.get("coupon"));
    }




    private void sendNotification2(Map<String,String> map, RemoteMessage remote,Bitmap bitmap){
        if(bitmap!=null){
            save();

            List<NotificationItem> nn = new ArrayList<>();
            SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh,Context.MODE_PRIVATE);
            String json = sharedPreferences.getString(constants.foodsinghNotif,"");
            SharedPreferences.Editor edit = sharedPreferences.edit();

            if(json.equals("")){

                nn.add(getNotificationItem(map,remote.getSentTime()));
                Gson gson = new Gson();
                NotificationLists li = new NotificationLists(nn);
                String tempJson = gson.toJson(li);
                edit.putString(constants.foodsinghNotif,tempJson);
                edit.apply();

            }else{
                Gson gson  = new Gson();
                NotificationLists li = gson.fromJson(json,NotificationLists.class);
                nn = li.getNotification();
                nn.add(getNotificationItem(map,remote.getSentTime()));
                li = new NotificationLists(nn);
                OnLog(li.getNotification());
                String tempJson = gson.toJson(li);
                edit.putString(constants.foodsinghNotif,tempJson);
                edit.apply();
            }

            final String title = map.get("title");
            String body = map.get("body");

            Context context = this;

            Intent intent= new Intent(this, NotificationActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

// Android 2.x does not support remote view + custom notification concept using
// support library
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                mBuilder.setSmallIcon(R.drawable.logo7);
                mBuilder.setContentTitle("FS")
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(body))
                        .setAutoCancel(true).setDefaults(Notification.DEFAULT_SOUND)
                        //.setLights(Color.WHITE, 500, 500)
                        .setContentText(remote.getNotification().getBody());
            } else {
                Log.i("please", "i was clled");

                mBuilder.setContentTitle(title);
                mBuilder.setContentText(body);
                if(bitmap==null)
                    mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.xx)).setBigContentTitle(title));
                else{
                    mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setBigContentTitle(title));
                }
                mBuilder.setSmallIcon(R.drawable.logo7);
                mBuilder.setAutoCancel(true);
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);

                // mBuilder.setLights(Color.WHITE, 500, 500);
            }//
// build notification
            mBuilder.setContentIntent(pendingIntent);
            mNotificationManager.notify((int)System.currentTimeMillis()%1000, mBuilder.build());
        }else{
            Log.i("android_notification","null notification received.");
        }
    }

    private void OnLog(List<NotificationItem> nn) {

        for(int i=0; i<nn.size(); i++){
            Log.i("please", nn.get(i).getBody()+","+nn.get(i).getNotificationType());
        }
    }

    private void save(){
        SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
        localdatabase.notifications = sharedPreferences.getInt(constants.notifAmount,0);
        localdatabase.notifications++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(constants.notifAmount,localdatabase.notifications);
        editor.apply();
        if(localdatabase.notifmount==null){
            Log.i("nullchecker"," null");

        }else{
            Log.i("nullchecker","not null");
        }
        Intent menu = new Intent();

        menu.setAction(constants.broaadcastReceiverMenu);
        sendBroadcast(menu);
        Intent notification = new Intent();
        notification.setAction(constants.broadCastReceiverNotification);
        sendBroadcast(notification);

        Intent cart = new Intent();
        cart.setAction(constants.broaadcastReceiverMenu2);
        sendBroadcast(cart);



    }




}
