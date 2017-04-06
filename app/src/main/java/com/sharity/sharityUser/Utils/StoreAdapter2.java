package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;
import com.sharity.sharityUser.R;

import java.util.ArrayList;



public class StoreAdapter2 extends RecyclerView.Adapter<StoreAdapter2.ViewHolder> {
    OnItemDonateClickListener listener;
    private int selectedPos=0;
    // private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<CharityDons> AL_id_text = new ArrayList<CharityDons>();
    Context mContext;

    public interface OnItemDonateClickListener {
        void onItemClick(int item,CharityDons bo);
    }

    public StoreAdapter2(Context c, ArrayList<CharityDons> AL_id_text, OnItemDonateClickListener listener) {
        mInflater = LayoutInflater.from(c);
        mContext = c;
        this.AL_id_text = AL_id_text;
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
    public StoreAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler_donation, null);
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

        final CharityDons bo = (CharityDons) getItem(position);
        viewHolder.bind(position,listener,bo);
        viewHolder.image.setTag(position);

        if(selectedPos == position){
            viewHolder.background.setBackgroundColor(Color.parseColor("#CBFFBD3A"));
        }else{
            viewHolder.background.setBackgroundColor(Color.TRANSPARENT);
        }



        if (bo.get_image()!=null) {
            byte[] image = bo.get_image();
            Bitmap PictureProfile = DbBitmapUtility.getImage(image);
            viewHolder.image.setImageBitmap(PictureProfile);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        RelativeLayout background;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            background = (RelativeLayout) itemView.findViewById(R.id.background);
        }


        public void bind(final int item, final OnItemDonateClickListener listener, final CharityDons bo) {
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