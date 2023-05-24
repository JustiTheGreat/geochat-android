package com.geochat.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.geochat.R;
import com.geochat.model.SearchItem;

import java.util.ArrayList;
import java.util.List;

public class SearchItemAdapter extends ArrayAdapter<SearchItem> {
    private final List<SearchItem> defaultData;
    private final Activity activity;

    public SearchItemAdapter(Context context, List<SearchItem> searchItems, Activity activity) {
        super(context, R.layout.search_item, searchItems);
        this.defaultData = new ArrayList<>(searchItems);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SearchItem> suggestions = new ArrayList<>();
//            if (constraint == null || constraint.toString().trim().length() == 0)
                suggestions.addAll(defaultData);
//            else defaultData.forEach(searchItem -> {
//                if (searchItem.getUsername().toLowerCase().contains(constraint.toString().toLowerCase().trim()))
//                    suggestions.add(searchItem);
//            });
            return new FilterResults() {{
                this.values = suggestions;
                this.count = suggestions.size();
            }};
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List<SearchItem>) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((SearchItem) resultValue).getUsername();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.R)
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchItem searchItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_item, parent, false);
        }

        TextView conversationName = convertView.findViewById(R.id.conversationName);
        conversationName.setText(searchItem.getUsername());

        ImageView flag = convertView.findViewById(R.id.flagImage);
        flag.setImageResource(R.drawable.romania_flag);

        TextView conversationType = convertView.findViewById(R.id.conversationType);
        conversationType.setText(activity.getString(R.string.person));

        return convertView;
    }
}


