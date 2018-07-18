package com.foodsingh.mindwires.foodsingh;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Random;

/**
 * Created by Tanmay on 01-10-2016.
 */
public class pagerAdapter extends PagerAdapter {
    LayoutInflater inflater;
    localdatabase database;
    ImageView imageView;
    private boolean doNotifyDataSetChangedOnce = false;

    public pagerAdapter(localdatabase database){
        this.database = database;
    }
    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup parent, int position){
        inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_pager_layout, parent, false);
        imageView = (ImageView)v.findViewById(R.id.view_pager_cover);
        Random rand = new Random();

               try{
                   Glide.with(parent.getContext()).load(database.BannerUrls.get(position%database.BannerUrls.size())).
                           crossFade().into(imageView);
               }catch(Exception e){
                imageView.setImageBitmap(BitmapFactory.decodeResource(imageView.getResources(),R.drawable.logo7));
               }

        Log.i("pager", "adapter has been called");
        parent.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
    }



    public int getPx(int r1,Context context){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r1, r.getDisplayMetrics());
        return (int)px;
    }
}


