package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterListUser extends ArrayAdapter<UserLocation> {

    public AdapterListUser(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterListUser(Context context, int resource, List<UserLocation> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.itemlistrow, null);
        }

        UserLocation p = getItem(position);
        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.user);
            CircleImageView imageView=(CircleImageView) v.findViewById(R.id.profil);

            if (tt1 != null) {
                tt1.setText(p.getUsername());
            }
            if (imageView!=null){
                Bitmap bmp = null;
                bmp = BitmapFactory.decodeByteArray(p.getPictureProfil(), 0, p.getPictureProfil().length);
                imageView.setImageBitmap(bmp);
            }
        }

        return v;
    }
    private void loadImages(ParseFile thumbnail, final ImageView img) {

        if (thumbnail != null) {
            thumbnail.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        img.setImageBitmap(bmp);
                    } else {
                    }
                }
            });
        } else {
            img.setImageResource(R.drawable.ic_cast_dark);
        }
    }// load image

}