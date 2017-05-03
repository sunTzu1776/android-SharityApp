package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharity.sharityUser.BO.LocationBusiness;
import com.sharity.sharityUser.BO.Promo;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;
import com.sharity.sharityUser.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;



public class AdapterPartenaireClient extends RecyclerView.Adapter<AdapterPartenaireClient.ViewHolder> {
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

    public AdapterPartenaireClient(String fragment, boolean type, Context c, ArrayList<LocationBusiness> AL_id_text, OnItemDonateClickListener listener) {
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
    public AdapterPartenaireClient.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View itemLayoutView=null;
        if (fragment.equals("client_Partenaire_list_fragment")){
            if (isShop){
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_partenaire_shop, null);

            }
            else {
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_partenaire_promo, null);
                Log.d("busss","row_partenaire_shop") ;
            }
        }

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


        shop = ((LocationBusiness) getItem(position));
        viewHolder.bind(position,listener,shop);

        if (fragment.equals("client_Partenaire_list_fragment")){
        }else {
            if(selectedPos == position){
                viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
            }else{
                viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
            }
        }


        if (isShop){
            if (shop.isoffset()){
                viewHolder.background.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.background.setVisibility(View.VISIBLE);
            }

            if (shop.getPicture()!=null) {
                byte[] image = shop.getPicture();
                viewHolder.image.setTag(position);
                if (image!=null){
                    Bitmap PictureProfile = DbBitmapUtility.getImage(image);
                    viewHolder.image.setImageBitmap(PictureProfile);
                }else {
                    viewHolder.image.setImageBitmap(null);
                }
            }

            if (viewHolder.distance!=null){
                viewHolder.distance.setText(String.format("%.0f",shop.getDistance())+"m");
            }
            Log.d("busss","passed") ;
            viewHolder.businessname.setText(shop.get_businessName());

        }else {
            if (shop.isoffset()){
                viewHolder.background.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.background.setVisibility(View.VISIBLE);
            }

            if (viewHolder.prix!=null){
                viewHolder.prix.setText(shop.getPrix());
            }
            if (viewHolder.nom!=null){
                viewHolder.nom.setText(shop.get_businessName());
            }
            if (viewHolder.description!=null){
                viewHolder.description.setText(shop.getDescription());
            }

            if (viewHolder.reduction!=null){
                viewHolder.reduction.setText(shop.getReduction());
            }
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView description, prix,reduction,nom;
        TextView distance, businessname;
        CircleImageView profil;
        RelativeLayout background;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (CircleImageView) itemView.findViewById(R.id.imageView);
            background = (RelativeLayout) itemView.findViewById(R.id.background);

            nom = (TextView) itemView.findViewById(R.id.nom);
            prix = (TextView) itemView.findViewById(R.id.prix);
            description = (TextView) itemView.findViewById(R.id.description);
            reduction = (TextView) itemView.findViewById(R.id.reduction);

            distance = (TextView) itemView.findViewById(R.id.distance);
            businessname = (TextView) itemView.findViewById(R.id.business_name);
            profil = (CircleImageView) itemView.findViewById(R.id.profil);
        }


        public void bind(final int item, final OnItemDonateClickListener listener, final Object bo) {
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