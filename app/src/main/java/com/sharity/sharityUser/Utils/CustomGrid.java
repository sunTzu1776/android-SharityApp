package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CustomGrid extends BaseAdapter{
      private Context mContext;
    LayoutInflater inflat;
    List<UserLocation> userLocations;
 
        public CustomGrid(Context c, List<UserLocation> userLocations) {
            inflat = LayoutInflater.from(c);
            mContext = c;
            this.userLocations=userLocations;
        }
 
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return userLocations.size();
        }
 
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return userLocations.get(position);
        }
 
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
 


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomGrid.ViewHolder holder = null;
        UserLocation it = userLocations.get(position);

        if (convertView == null) {
            holder = new CustomGrid.ViewHolder();

                convertView = inflat.inflate(R.layout.adapter_gridview_paimentpro, null);
            holder.textView = (TextView) convertView.findViewById(R.id.grid_text);
            holder.imageView = (CircleImageView) convertView.findViewById(R.id.grid_image);


            convertView.setTag(holder);

        } else {
            holder = (CustomGrid.ViewHolder) convertView.getTag();
        }

        if (it != null) {
                if(holder.textView!= null) {
                    holder.textView.setText(it.getUsername());
                }

            if (holder.imageView != null) {
                Bitmap bmp = null;
                bmp = BitmapFactory.decodeByteArray(it.getPictureProfil(), 0, it.getPictureProfil().length);
                holder.imageView.setImageBitmap(bmp);
            }

        }
        return convertView;
    }


    private class ViewHolder {
        TextView  textView;
        CircleImageView imageView;

        public String toString() {
            return "-";
        }
    }
}