package com.foodsingh.mindwires.foodsingh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRANSHOO VERMA on 15/09/2017.
 */

public class order_history_Adapter extends RecyclerView.Adapter<order_history_Adapter.ViewHolder> {

   private final String[] months  = {"January", "February", "March", "April", "May", "June", "July", "August", "September","October", "November", "December"};

    Context mContext;
    List<String> date=new ArrayList<>();
    List<String> amount=new ArrayList<>();
    List<String> address=new ArrayList<>();
    int counter=0;
    List<String> orders=new ArrayList<>();
    List<String> id = new ArrayList<>();


    public order_history_Adapter(Context mContext, List<String> date, List<String> amount, List<String> address, List<String> orders,List<String> id) {
        this.mContext = mContext;
        this.date = date;
        this.id = id;
        this.amount = amount;
        this.address = address;
        this.orders = orders;
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView date_text,amount_text,orders_text_,address_text_,order_no;
        FrameLayout arrow;

        public ViewHolder(View itemView)
        {
            super(itemView);

            date_text=(TextView) itemView.findViewById(R.id.date);
            order_no=(TextView) itemView.findViewById(R.id.order_number);
       //     arrow=(TextView) itemView.findViewById(R.id.textView_amount);
            arrow=(FrameLayout) itemView.findViewById(R.id.frame_click);
            orders_text_=(TextView) itemView.findViewById(R.id.textview_scroll_items);
            address_text_=(TextView) itemView.findViewById(R.id.textview_scroll_price);
            amount_text=(TextView) itemView.findViewById(R.id.textview_scroll_price);
            Typeface t = Typeface.createFromAsset(date_text.getContext().getAssets(), "fonts/OratorStd.otf");
            date_text.setTypeface(t);

            orders_text_.setTypeface(t);
            order_no.setTypeface(t);
            address_text_.setTypeface(t);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(mContext).inflate(R.layout.cardview_order_history,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String d=date.get(holder.getAdapterPosition());
        final String a=amount.get(holder.getAdapterPosition());
        final String o=orders.get(holder.getAdapterPosition());
        final String add=address.get(holder.getAdapterPosition());
        String[] temp = d.split("-");

        final String year = temp[0];

        final String month = months[Integer.parseInt(temp[1])-1];
        final String day = temp[2];


        holder.date_text.setText(month+" "+day.substring(0,2)+", "+year);
        holder.amount_text.setText(a);

        //holder.orders_text.setText(o);
        //holder.address_text.setText(add);
       // holder.order_qt.setText((holder.getAdapterPosition()+1)+"");
        holder.order_no.setText(id.get(holder.getAdapterPosition()));

        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              FoodItem item = new FoodItem(id.get(holder.getAdapterPosition()),o,a,add,month+" "+day.substring(0,2)+", "+year);
                Intent i = new Intent(holder.arrow.getContext(), Tracker.class);
                Bundle b = new Bundle();
                b.putParcelable("object",item);
                b.putBoolean("getter",false);
                i.putExtras(b);
                holder.arrow.getContext().startActivity(i);
            }
        });


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return amount.size();
    }

    private void Display(Context c, String s){
        //Toast.makeText(c, s, Toast.LENGTH_LONG).show();
    }
}
