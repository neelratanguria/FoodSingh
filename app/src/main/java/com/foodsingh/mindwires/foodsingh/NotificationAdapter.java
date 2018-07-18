package com.foodsingh.mindwires.foodsingh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanmay on 20-10-2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    List<NotificationItem> notificationItems = new ArrayList<>();
    Context c;
     NotificationItem item=null;
    static int i=0;

    public NotificationAdapter(List<NotificationItem> mainList,Context c){
        this.notificationItems = mainList;
        this.c = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        localdatabase.cache=0;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationcard, parent,false);

        return new ViewHolder(v);
    }


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;

    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        item= notificationItems.get(holder.getAdapterPosition());


        if(item.getUrl().equals("-")){
            holder.knowmore1.setVisibility(View.INVISIBLE);
            holder.knowmore2.setVisibility(View.INVISIBLE);
        }
        else{
            holder.knowmore1.setVisibility(View.VISIBLE);
            holder.knowmore2.setVisibility(View.VISIBLE);
        }
        if(item.getCoupon().equals("-")){
            holder.copycode.setVisibility(View.INVISIBLE);
            holder.copycode2.setVisibility(View.INVISIBLE);
        }else{
            holder.copycode.setVisibility(View.VISIBLE);
            holder.copycode2.setVisibility(View.VISIBLE);
        }

        if(item.getNotificationType().equals("1")){
            holder.l2.setVisibility(View.GONE);
            holder.l22.setVisibility(View.GONE);
            holder.l222.setVisibility(View.GONE);
            holder.l1.setVisibility(View.VISIBLE);
            holder.l11.setVisibility(View.VISIBLE);
            holder.l111.setVisibility(View.VISIBLE);
            holder.time1.setText(item.getTime()+"");
            holder.title1.setText(item.getTitle());

            holder.body1.setText(item.getBody());
            holder.time1.setText(getTimeAgo(Long.parseLong(item.getTime()),holder.time1.getContext()));
            Glide.with(holder.img1.getContext()).load(item.getImg()).placeholder(R.drawable.whatsapp).thumbnail(0.01f).into(holder.img1);
        }else if(item.getNotificationType().equals("2")){

                holder.l1.setVisibility(View.GONE);
                holder.l11.setVisibility(View.GONE);
                holder.l111.setVisibility(View.GONE);
                holder.l2.setVisibility(View.VISIBLE);
                holder.l22.setVisibility(View.VISIBLE);
                holder.l222.setVisibility(View.VISIBLE);
            holder.time2.setText(getTimeAgo(Long.parseLong(item.getTime()),holder.time2.getContext()));

                holder.title2.setText(item.getTitle());
                holder.body2.setText(item.getBody());

                Glide.with(holder.img2.getContext()).load(item.getImg()).placeholder(R.drawable.whatsapp).thumbnail(0.01f).into(holder.img2);
            }

            if(item.getRead()){
               // holder.title1.setTypeface(null, Typeface.NORMAL);
                holder.body1.setTypeface(null, Typeface.NORMAL);
                //holder.title2.setTypeface(null, Typeface.NORMAL);
                holder.body2.setTypeface(null, Typeface.NORMAL);

               localdatabase.cache++;




            }else{

                i++;

                holder.title1.setTypeface(null, Typeface.BOLD);
                holder.body1.setTypeface(null, Typeface.BOLD);
                holder.title2.setTypeface(null, Typeface.BOLD);
                holder.body2.setTypeface(null, Typeface.BOLD);



            }

            holder.knowmore2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i--;

                    if(i==0){

                    }else{

                    }

                    item = notificationItems.get(holder.getAdapterPosition());
                    item.setRead(true);
                    notificationItems.remove(item);
                    notificationItems.add(holder.getAdapterPosition(),item);
                    save();
                    decrement();
                    holder.body1.setTypeface(null, Typeface.NORMAL);
                    //holder.title2.setTypeface(null, Typeface.NORMAL);
                    holder.body2.setTypeface(null, Typeface.NORMAL);

                    String url=item.getUrl();
                    Bundle a=new Bundle();
                    a.putString("url",url);
                    Intent intent=new Intent(c.getApplicationContext(),web_view.class);
                    intent.putExtras(a);
                    c.startActivity(intent);
                }
            });
        holder.knowmore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i--;


                item.setRead(true);
                notificationItems.remove(item);
                notificationItems.add(holder.getAdapterPosition(),item);


                save();
                decrement();
                holder.body1.setTypeface(null, Typeface.NORMAL);
                //holder.title2.setTypeface(null, Typeface.NORMAL);
                holder.body2.setTypeface(null, Typeface.NORMAL);
                String url=item.getUrl();
                Bundle a=new Bundle();
                a.putString("url",url);
                Intent intent=new Intent(c.getApplicationContext(),web_view.class);
                intent.putExtras(a);
                c.startActivity(intent);
            }
        });

        holder.copycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClipboard(view.getContext(),item.getCoupon());
                Display("Coupon code copied to clipboard.");
                item.setRead(true);
                notificationItems.remove(item);
                notificationItems.add(holder.getAdapterPosition(),item);


                save();
                decrement();

            }
        });
        holder.copycode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClipboard(view.getContext(),item.getCoupon());
                Display("Coupon code copied to clipboard.");
                item.setRead(true);
                notificationItems.remove(item);
                notificationItems.add(holder.getAdapterPosition(),item);


                save();
                decrement();

            }
        });

        Log.i("imagenotif",item.getImg());
    }



        private void setClipboard(Context context, String text) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
                clipboard.setPrimaryClip(clip);
            }

        }
    private void save() {

        SharedPreferences sharedPreferences = c.getSharedPreferences(constants.foodsingh,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Gson gson = new Gson();
        NotificationLists nl = new NotificationLists(notificationItems);
        String tempJson = gson.toJson(nl);
        edit.putString(constants.foodsinghNotif,tempJson);
        edit.apply();
    }


    private void decrement(){
        SharedPreferences sharedPreferences = c.getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
        localdatabase.notifications = sharedPreferences.getInt(constants.notifAmount,0);
        if(localdatabase.notifications!=0) {
            localdatabase.notifications--;
            if(localdatabase.notifications!=0) {
             // localdatabase.notifmount.setVisibility(View.VISIBLE);
              // localdatabase.notifmount.setText(localdatabase.notifications + "");
            }else{
             //localdatabase.notifmount.setVisibility(View.INVISIBLE);
            }
        }else{

        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(constants.notifAmount,localdatabase.notifications);
        editor.apply();

    }

    @Override
    public int getItemCount() {
        return notificationItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout l1,l2,l11,l111,l22,l222;
        ImageView img1, img2;
        TextView title1, title2, body1, body2, copycode, knowmore1, knowmore2,time1,time2, copycode2;

        public ViewHolder(View itemView) {
            super(itemView);
            l1 = (LinearLayout)itemView.findViewById(R.id.l1);
            l2 = (LinearLayout)itemView.findViewById(R.id.l2);
            l11 = (LinearLayout)itemView.findViewById(R.id.l11);
            l111 = (LinearLayout)itemView.findViewById(R.id.l111);
            l22 = (LinearLayout)itemView.findViewById(R.id.l22);
            l222 = (LinearLayout)itemView.findViewById(R.id.l222);
            img1 = (ImageView)itemView.findViewById(R.id.image1);
            img2= (ImageView)itemView.findViewById(R.id.image2);
            title1 = (TextView) itemView.findViewById(R.id.title1);
            title2 = (TextView)itemView.findViewById(R.id.title2);
            body1 = (TextView)itemView.findViewById(R.id.content1text);
            body2 = (TextView)itemView.findViewById(R.id.content2text);
            copycode = (TextView)itemView.findViewById(R.id.copycode);
            copycode2 = (TextView)itemView.findViewById(R.id.copycode2);
            knowmore1 = (TextView)itemView.findViewById(R.id.knowmore1);
            knowmore2 = (TextView)itemView.findViewById(R.id.knowmore2);
            time1 = (TextView)itemView.findViewById(R.id.time1);
            time2 = (TextView)itemView.findViewById(R.id.time2);
        }
    }

    private void Display(String s){
        Toast.makeText(c,s,Toast.LENGTH_LONG).show();
    }

}
