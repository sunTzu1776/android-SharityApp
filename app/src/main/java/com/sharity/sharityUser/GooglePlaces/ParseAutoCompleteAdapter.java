package com.sharity.sharityUser.GooglePlaces;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.sharity.sharityUser.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ParseAutoCompleteAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<ParsePlace> items;
    private ArrayList<ParsePlace> filteredItems;
    private ItemFilter mFilter = new ItemFilter();
    private ArrayList<ParsePlace> resultList;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyDdBXFxMBhCBO1XmB8c9MEQ1bsigXuGIwg";


    public ParseAutoCompleteAdapter(Context context) {
        //super(context, R.layout.your_row, items);
        this.context = context;
    }

    @Override
    public int getCount() {
        if(filteredItems==null){
            Log.v("LOG","Warn, null filteredData");
            return 0;
        }else{
            return filteredItems.size();
        }
    }


    @Override
    public Object getItem(int position) {
        return filteredItems.get(position).getDescription();
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

            convertView = inflater.inflate(R.layout.row_location_autocomplete, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ParsePlace it = filteredItems.get(position);
        if (!it.getDescription().isEmpty() || viewHolder != null) {

            viewHolder.description.setText(it.getDescription());
            viewHolder.id.setText(it.getPlace_id());

        }
        return convertView;
    }

    public static class ViewHolder {
        TextView description,id;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            List<ParsePlace> tempItems = new ArrayList<>();

            if (constraint != null) {
                // Retrieve the autocomplete results.

                tempItems = autocomplete(constraint
                        .toString());

                // Assign the data to the FilterResults
                results.values = tempItems;
                results.count = tempItems.size();
            }

            results.values = tempItems;
            results.count = tempItems.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (ArrayList<ParsePlace>) results.values;
            notifyDataSetChanged();
        }
    }


    public void remove() {
        filteredItems.clear();

    }

    public Filter getFilter() {
        return mFilter;
    }

    public static ArrayList<ParsePlace> autocomplete(String input) {

        ArrayList<ParsePlace> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE
                    + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?sensor=false&key=" + API_KEY);
            // sb.append("&components=country:uk");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("TAG", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("TAG", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");


            // Extract the Place descriptions from the results
            resultList = new ArrayList<ParsePlace>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(new ParsePlace(predsJsonArray.getJSONObject(i).getString(
                        "description"),predsJsonArray.getJSONObject(i).getString(
                        "place_id")));


            }

        } catch (JSONException e) {
            Log.e("TAG", "Cannot process JSON results", e);
        }

        return resultList;
    }

}

