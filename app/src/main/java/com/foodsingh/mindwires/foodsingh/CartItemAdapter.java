package com.foodsingh.mindwires.foodsingh;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRANSHOO VERMA on 13/09/2017.
 */

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

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

    public CartItemAdapter(Context mContext, List<MenuItems> menuItems) {
        this.mContext = mContext;
        this.menuItems = menuItems;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView diname,diprice,item_quantity, total_diprice;
        ImageView pl,mi, delete;
        public ViewHolder(View itemView) {
            super(itemView);
            diname=(TextView) itemView.findViewById(R.id.dish_name_slide);
            diprice=(TextView) itemView.findViewById(R.id.dish_price);
            total_diprice=(TextView)itemView.findViewById(R.id.total_dish_price);
            item_quantity=(TextView) itemView.findViewById(R.id.item_quantity_slide);
            delete=(ImageView) itemView.findViewById(R.id.delete);
            pl=(ImageView) itemView.findViewById(R.id.plus_slide);
            mi=(ImageView) itemView.findViewById(R.id.minus_slide);
            Typeface t = Typeface.createFromAsset(diname.getContext().getAssets(), "fonts/MTCORSVA.TTF");
            diname.setTypeface(t);
            diprice.setTypeface(t);
            item_quantity.setTypeface(t);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.cardview_cart_item,parent,false);
        ViewHolder vh=new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        plus=(ImageView) holder.itemView.findViewById(R.id.plus_slide);
        minus=(ImageView) holder.itemView.findViewById(R.id.minus_slide);

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
        holder.diprice.setText("₹"+rupees);
        if(rupees.equals("0")){
            holder.diprice.setText("NA");
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
                if(!holder.diprice.getText().toString().equals("NA")){
                    int quantity = item.getQuantity();
                    quantity++;
                    item.setQuantity(quantity);
                    holder.item_quantity.setText(String.valueOf(quantity));
                    holder.total_diprice.setText("₹"+Integer.parseInt(item.getPrice())*quantity);
                    /*if(!localdatabase.cartList.contains(item)){
                        localdatabase.cartList.add(item);
                    } */
                    cart.calculateTotal();
                    cart.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                    cart.updateCartIcon();
                    menu.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                }

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!holder.diprice.getText().toString().equals("NA") && item.getQuantity()!=0){
                    int quantity = item.getQuantity();
                    quantity--;
                    item.setQuantity(quantity);
                    holder.item_quantity.setText(String.valueOf(quantity));
                    holder.total_diprice.setText("₹"+Integer.parseInt(item.getPrice())*quantity);
                    if(quantity == 0){
                        //localdatabase.cartList.remove(item);
                        int pos = checkCart(item);
                        if(pos != -1){
                            localdatabase.cartList.remove(pos);
                            cart.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                            menu.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                            cart.updateCartIcon();
                        }
                        cart.adapter.notifyDataSetChanged();
                    }
                    cart.calculateTotal();
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setQuantity(0);
                //Toast.makeText(mContext, ""+item.getQuantity(), Toast.LENGTH_SHORT).show();
                //localdatabase.cartList.remove(item);
                int pos = checkCart(item);
                if(pos != -1){
                    localdatabase.cartList.remove(pos);

                }
                cart.sidesAdapter.notifyDataSetChanged();
                cart.adapter.notifyDataSetChanged();
                if(menu_category_wise.cartitemcount1 != null) {
                    cart.updateCartIcon();
                }
                cart.calculateTotal();
                cart.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
                menu.cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
            }
        });
/*
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.diprice.getText().toString().equals("NA")){

                }else{

                //adding to the list
                constants.items_name.add(dish_name.get(position));
                constants.items_price.add(dish_price.get(position));

                //setting the value to the textView for having the quantity
                int a=Integer.parseInt((String) holder.item_quantity.getText())+1;
                holder.item_quantity.setText(String.valueOf(a));

                //showing at the toolbar
                menu_category_wise.cartitemcount.setText(String.valueOf(constants.items_name.size()));
                menu.cartitemcount1.setText(String.valueOf(constants.items_name.size()));
            //    Toast.makeText(mContext, "Added to cart", Toast.LENGTH_SHORT).show();


                //making the quantity count
                if(constants.item_name_deb.contains(dish_name.get(position)))
                {

                    int index= constants.item_name_deb.indexOf(dish_name.get(position));
                    int prev_value= Integer.parseInt(constants.item_quant_deb.get(index));
                    constants.item_quant_deb.set(index,String.valueOf(prev_value+1));
              //      Toast.makeText(mContext, constants.item_name_deb.toString()+"\n"+constants.item_quant_deb, Toast.LENGTH_SHORT).show();

                }
                else
                {
                    //add that item to the arraylist
                    constants.item_name_deb.add(dish_name.get(position));
                    constants.item_quant_deb.add("1");
          //          Toast.makeText(mContext, "added 1", Toast.LENGTH_SHORT).show();
                }

                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.diprice.getText().toString().equals("NA")){

                }else{
                int a=Integer.parseInt((String) holder.item_quantity.getText());
                if(a!=0)
                {
                    a=a-1;
                    holder.item_quantity.setText(String.valueOf(a));
                }


                if(constants.item_name_deb.contains(dish_name.get(position)))
                {

                    int index= constants.item_name_deb.indexOf(dish_name.get(position));
                    int prev_value= Integer.parseInt(constants.item_quant_deb.get(index));

                    if(prev_value==1)
                    {
                        constants.item_name_deb.remove(dish_name.get(position));
                        constants.item_quant_deb.remove(index);
                    }
                    else {
                        constants.item_quant_deb.set(index, String.valueOf(prev_value - 1));
                    }
          //          Toast.makeText(mContext, constants.item_name_deb.toString() + "\n" + constants.item_quant_deb, Toast.LENGTH_SHORT).show();

                }


                if(constants.items_name.contains(dish_name.get(position)))
                {
                    constants.items_name.remove(dish_name.get(position));
                    constants.items_price.remove(dish_price.get(position));
                    menu_category_wise.cartitemcount.setText(String.valueOf(constants.items_name.size()));
                    menu.cartitemcount1.setText(String.valueOf(constants.items_name.size()));
            //        Toast.makeText(mContext, "Removed from cart", Toast.LENGTH_SHORT).show();
                }
                else {
                    //        Toast.makeText(mContext, "You dont have this item in cart", Toast.LENGTH_SHORT).show();
                }        }
            }
        });
        */
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
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



///adlasdjlakdjlaskdkj