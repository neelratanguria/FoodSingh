package com.foodsingh.mindwires.foodsingh;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TANMAY on 13/09/2017.
 */

public class Sides_Adapter extends RecyclerView.Adapter<Sides_Adapter.ViewHolder> {

    private Context mContext;

    private  List<MenuItems> menuItems = new ArrayList<>();

    private List<String> NA = new ArrayList<>();
    boolean check=false;

    ImageView plus,minus;


/*
    public MenuItemAdapter(Context mContext, List<String> dish_name, List<String> dish_price) {
        this.mContext=mContext;
        this.dish_name=dish_name;
        this.dish_price=dish_price;
    }

*/

    public Sides_Adapter(Context mContext, List<MenuItems> menuItems) {
        this.mContext = mContext;
        //Display(mContext, "called con");
        this.menuItems = menuItems;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView diname,item_quantity, total_diprice;
        ImageView pl,mi,pic;
        public ViewHolder(View itemView) {
            super(itemView);
            diname=(TextView) itemView.findViewById(R.id.dish_name_slide2);
            total_diprice=(TextView) itemView.findViewById(R.id.dish_price2);
            pic = (ImageView)itemView.findViewById(R.id.img_item_slide2);
            item_quantity=(TextView) itemView.findViewById(R.id.item_quantity_slide2);
          //  delete=(ImageView) itemView.findViewById(R.id.delete2);
            pl=(ImageView) itemView.findViewById(R.id.plus_slide2);
            mi=(ImageView) itemView.findViewById(R.id.minus_slide2);
            Typeface t = Typeface.createFromAsset(diname.getContext().getAssets(), "fonts/MTCORSVA.TTF");
            diname.setTypeface(t);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.slides_card,parent,false);
        ViewHolder vh=new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        plus=(ImageView) holder.itemView.findViewById(R.id.plus_slide2);
        minus=(ImageView) holder.itemView.findViewById(R.id.minus_slide2);
        //Display(plus.getContext(), "Called here");
        /*
        String name=dish_name.get(position);
        String rupees=dish_price.get(position);
        */

        final MenuItems item = menuItems.get(position);
        String name = item.getName();
        String rupees = item.getPrice();
        int qty = item.getQuantity();
        holder.diname.setText(name);
        holder.total_diprice.setText("₹"+Integer.parseInt(rupees)*qty);
        holder.item_quantity.setText(""+qty);
        holder.total_diprice.setText("₹"+item.getPrice());
        Glide.with(holder.pic.getContext()).load(item.getImage()).thumbnail(0.01f).into(holder.pic);
        //holder.diprice.setText("₹"+rupees);
        if(rupees.equals("0")){
         //   holder.diprice.setText("NA");
            check = true;
        }else{
            check = false;
        }


        if(constants.item_name_deb.contains(name))
        {
            int index= constants.item_name_deb.indexOf(name);
            int qa= Integer.parseInt(constants.item_quant_deb.get(index));
            holder.item_quantity.setText(String.valueOf(qa));
        }

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.total_diprice.getText().toString().equals("NA")){
                    int quantity = item.getQuantity();
                    quantity++;
                    item.setQuantity(quantity);
                    holder.item_quantity.setText(String.valueOf(quantity));
                    //holder.total_diprice.setText("₹"+Integer.parseInt(item.getPrice())*quantity);
                    if(checkCart(item) == -1){
                        localdatabase.cartList.add(item);
                    }
                    cart.adapter.notifyDataSetChanged();
                    cart.calculateTotal();
                    cart.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                    menu.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                }

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!holder.total_diprice.getText().toString().equals("NA") && item.getQuantity()!=0){
                    int quantity = item.getQuantity();
                    quantity--;
                    item.setQuantity(quantity);
                    holder.item_quantity.setText(String.valueOf(quantity));
                    if(quantity == 0){
                        //localdatabase.cartList.remove(item);
                        int pos = checkCart(item);
                        if(pos != -1){
                            localdatabase.cartList.remove(pos);
                        }
                    }
                    cart.adapter.notifyDataSetChanged();
                    cart.calculateTotal();
                    cart.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                    menu.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                }
            }
        });




    }


    private void Display(Context c, String s){
        Toast.makeText(c,s, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {

        //Display(mContext,menuItems.size()+"");
        return menuItems.size();
    }

    private int checkCart(MenuItems item){
        for(int i=0; i<localdatabase.cartList.size(); i++){
            if(localdatabase.cartList.get(i).getId().equals(item.getId())
                    && localdatabase.cartList.get(i).getName().equals(item.getName())
                    ){
                return i;
            }
        }
        return -1;
    }
}
