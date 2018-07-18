package com.foodsingh.mindwires.foodsingh;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRANSHOO VERMA on 15/09/2017.
 */

public class backupclass extends RecyclerView.Adapter<backupclass.ViewHolder> {

    Context mContext;
    List<String> date=new ArrayList<>();
    List<String> amount=new ArrayList<>();
    List<String> address=new ArrayList<>();
    int counter=0;
    List<String> orders=new ArrayList<>();

    public backupclass(Context mContext, List<String> date, List<String> amount, List<String> address, List<String> orders) {
        this.mContext = mContext;
        this.date = date;
        this.amount = amount;
        this.address = address;
        this.orders = orders;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView date_text,amount_text,orders_text_,address_text_,order_no,arrow;

        public ViewHolder(View itemView)
        {
            super(itemView);

            date_text=(TextView) itemView.findViewById(R.id.date);
            order_no=(TextView) itemView.findViewById(R.id.order_number);
            arrow=(TextView) itemView.findViewById(R.id.textView_amount);
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
    public void onBindViewHolder(ViewHolder holder, int position) {

        String d=date.get(holder.getAdapterPosition());
        String a=amount.get(holder.getAdapterPosition());
        String o=orders.get(holder.getAdapterPosition());
        String add=address.get(holder.getAdapterPosition());

        holder.date_text.setText(d);
        // holder.amount_text.setText(a);
        //holder.orders_text.setText(o);
        //holder.address_text.setText(add);
        // holder.order_qt.setText((holder.getAdapterPosition()+1)+"");

        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Display(view.getContext(),"Clicked");
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
        Toast.makeText(c, s, Toast.LENGTH_LONG).show();
    }
}