package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharity.sharityUser.BO.LocationBusiness;
import com.sharity.sharityUser.BO.Promo;
import com.sharity.sharityUser.BO.Shop;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;
import com.sharity.sharityUser.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.value;
import static com.sharity.sharityUser.R.id.latitude;
import static com.sharity.sharityUser.R.id.longitude;
import static com.sharity.sharityUser.R.id.nom;
import static com.sharity.sharityUser.R.id.reduction;


public class AdapterPartenaireMapClient extends RecyclerView.Adapter<AdapterPartenaireMapClient.ViewHolder> {
    OnItemDonateClickListener listener;
    String fragment;
    private int selectedPos=0;
    // private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<LocationBusiness> AL_id_text = new ArrayList<LocationBusiness>();
    Context mContext;
    private LocationBusiness shop;
    boolean isShop;

    public interface OnItemDonateClickListener {
        void onItemClick(int item, Object bo);
    }

    public AdapterPartenaireMapClient(String fragment, boolean type, Context c, ArrayList<LocationBusiness> AL_id_text, OnItemDonateClickListener listener) {
        mInflater = LayoutInflater.from(c);
        this.fragment=fragment;
        isShop=type;
        mContext = c;
        this.AL_id_text =AL_id_text;
        this.listener = listener;
    }

    public int getCount() {
        return AL_id_text.size();
    }

    public Object getItem(int position) {
        return AL_id_text.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    @Override
    public AdapterPartenaireMapClient.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        View itemLayoutView=null;
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_partenaire_horizontal, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return AL_id_text.size();
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {



            shop =(LocationBusiness) getItem(position);
            viewHolder.bindShop(position,listener,shop);

        if (fragment.equals("client_Partenaire_list_fragment")){
        }else {
           /* if(selectedPos == position){
                viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
            }else{
                viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
            }*/
        }


        if (isShop){
            if (shop.isoffset()){
                viewHolder.background.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.background.setVisibility(View.VISIBLE);
            }

            //distance
            if (viewHolder.prix!=null){
                viewHolder.prix.setText(String.format("%.0f",shop.getDistance())+" m");
            }

            if (viewHolder.image!=null){
                viewHolder.image.setVisibility(View.VISIBLE);
                viewHolder.image.setTag(position);
                viewHolder.reduction.setVisibility(View.INVISIBLE);
                byte[] image = shop.getPicture();
                if (image!=null){
                    Bitmap PictureProfile = DbBitmapUtility.getImage(image);
                    viewHolder.image.setImageBitmap(PictureProfile);
                }else {
                    viewHolder.image.setImageBitmap(null);
                }
            }

            if (viewHolder.businessname!=null){
                viewHolder.businessname.setText(shop.get_businessName());
            }

            if (viewHolder.latitude!=null){
                viewHolder.latitude.setText(String.valueOf(shop.get_latitude()));
            }
            if (viewHolder.longitude!=null){
                viewHolder.longitude.setText(String.valueOf(shop.get_longitude()));
            }


        }else {
            if (shop.isoffset()){
                viewHolder.background.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.background.setVisibility(View.VISIBLE);
            }

            if (shop.getDescription()!=null) {
            }

            if (viewHolder.image!=null){
              /*  byte[] image = promo.getPicture();
                if (image!=null){
                    Bitmap PictureProfile = DbBitmapUtility.getImage(image);
                    viewHolder.image.setTag(position);
                    viewHolder.image.setImageBitmap(PictureProfile);
                }*/
            }

            if (viewHolder.reduction!=null){
                viewHolder.image.setVisibility(View.INVISIBLE);
                viewHolder.reduction.setVisibility(View.VISIBLE);
                viewHolder.reduction.setText(shop.getReduction());
            }
            if (viewHolder.prix!=null){
                viewHolder.prix.setText(shop.getPrix());
            }

            if (viewHolder.businessname!=null){
                viewHolder.businessname.setText(shop.get_businessName());
            }

            if (viewHolder.description!=null){
                viewHolder.description.setText(shop.getDescription());
            }

            if (viewHolder.latitude!=null){
                viewHolder.latitude.setText(String.valueOf(shop.get_latitude()));
            }
            if (viewHolder.longitude!=null){
                viewHolder.longitude.setText(String.valueOf(shop.get_longitude()));
            }
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView description, prix,businessname;
        TextView latitude, longitude, reduction;
        RelativeLayout background;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (CircleImageView) itemView.findViewById(R.id.imageView);
            description = (TextView) itemView.findViewById(R.id.description);
            businessname = (TextView) itemView.findViewById(R.id.business_name);
            prix = (TextView) itemView.findViewById(R.id.prix);
            latitude = (TextView) itemView.findViewById(R.id.latitude);
            longitude = (TextView) itemView.findViewById(R.id.longitude);
            reduction = (TextView) itemView.findViewById(R.id.reduction);
            background=(RelativeLayout)itemView.findViewById(R.id.background);


            background.getLayoutParams().width = (int) (Utils.getScreenWidth(itemView.getContext()) / 2);
            background.getLayoutParams().height = (int) (Utils.getScreenWidth(itemView.getContext()) / 2);


        }


        public void bindShop(final int item, final OnItemDonateClickListener listener, final LocationBusiness bo) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        listener.onItemClick(item, bo);
                    notifyItemChanged(selectedPos);
                    selectedPos = item;
                    notifyItemChanged(selectedPos);
                }
            });
        }

        public void bindPromo(final int item, final OnItemDonateClickListener listener, final Promo bo) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        listener.onItemClick(item, bo);
                    notifyItemChanged(selectedPos);
                    selectedPos = item;
                    notifyItemChanged(selectedPos);
                }
            });
        }
    }
}