package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.History;
import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.fragment.pro.Pro_LocationUserList_fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.google.android.gms.analytics.internal.zzy.i;
import static com.google.android.gms.analytics.internal.zzy.p;
import static com.google.android.gms.analytics.internal.zzy.v;
import static com.sharity.sharityUser.Application.getContext;
import static com.sharity.sharityUser.R.id.price;
import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.activity.LocationUserActivity.Pro_Location;
import static com.sharity.sharityUser.activity.LocationUserActivity.db;
import static com.sharity.sharityUser.activity.LocationUserActivity.parseUser;
import static com.sharity.sharityUser.fragment.pro.Pro_History_fragment.JSON;


public class AdapterListUser extends BaseAdapter {
    private UserLocation p;
    private Context context;
    private ArrayList<UserLocation> items;
    ToastInterface toastInterface;
    OkHttpClient client = new OkHttpClient();
    public AdapterListUser(Context context, ArrayList<UserLocation> items, ToastInterface toastInterface) {
        //super(context, R.layout.your_row, items);
        this.toastInterface=toastInterface;
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        if (items == null) {
            Log.v("LOG", "Warn, null filteredData");
            return 0;
        } else {
            return items.size();
        }
    }


    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.row_closeuser_location_listiew, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.userTV = (TextView) convertView.findViewById(user);
            viewHolder.priceET = (EditText) convertView.findViewById(price);
            viewHolder.send_BT = (Button) convertView.findViewById(R.id.send);
            viewHolder.imageView = (CircleImageView) convertView.findViewById(R.id.profil);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        p = items.get(position);
        viewHolder.send_BT.setTag(position);
        viewHolder.send_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewHolder.priceET.getText().toString().length() > 0) {
                    SendNotification(p,viewHolder.priceET);
                } else {
                    Toast.makeText(getContext(), "Veuillez entrer une valeur", Toast.LENGTH_LONG).show();
                }
            }
        });
        if (viewHolder.userTV != null) {
            viewHolder.userTV.setText(p.getUsername());
        }
        if (viewHolder.imageView != null) {
            Bitmap bmp = null;
            bmp = BitmapFactory.decodeByteArray(p.getPictureProfil(), 0, p.getPictureProfil().length);
            viewHolder.imageView.setImageBitmap(bmp);
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView userTV;
        EditText priceET;
        Button send_BT;
        CircleImageView imageView;
    }


    Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")
                .addHeader("X-Parse-Application-Id","process.env.PARSE_APP_ID")
                .addHeader("X-Parse-Master-Key","process.env.PARSE_MASTER_KEY")
                .addHeader("X-Parse-Session-Token",parseUser.getSessionToken())
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    private void SendNotification(UserLocation user, final EditText price){
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject param = new JSONObject();
            JSONObject data = new JSONObject();
            param.put("title", "Notification");
            param.put("body", db.getBusinessName()+ " vous à envoyé une notification ");
            data.put("data", param);
            jsonObject.put("payload", data);
            Log.d("jsonout",jsonObject.toString());
            post("http://ec2-52-56-157-252.eu-west-2.compute.amazonaws.com/api/push/"+user.getId(), jsonObject.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("Response Push Failed", e.getMessage());
                            toastInterface.onNotificationError();

                            //Something went wrong
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String responseStr = response.body().string();
                                Log.d("Response", responseStr);

                                if (Pro_Location!=null){
                                    Pro_Location.runOnUiThread(new Runnable() {
                                        public void run() {
                                            toastInterface.onNotificationSuccess(p,price.getText().toString());
                                        }
                                    });
                                }
                            } else {
                               // toastInterface.onNotificationError();
                                if (Pro_Location!=null){
                                    Pro_Location.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getContext(), "Une erreur s'est produite lors de la notification", Toast.LENGTH_LONG).show();
                                            //  toastInterface.onNotificationSuccess();
                                        }
                                    });
                                }
                                Log.d("Response Push Failed", response.message());
                            }
                        }
                    }
            );
        } catch (JSONException ex) {
            Log.d("Exception", "JSON exception", ex);
        }
    }
}

