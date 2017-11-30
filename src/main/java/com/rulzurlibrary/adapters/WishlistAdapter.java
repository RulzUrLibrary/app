package com.rulzurlibrary.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulzurlibrary.R;
import com.rulzurlibrary.common.Wishlist;

import java.util.List;

public class WishlistAdapter extends ArrayAdapter<Wishlist> {
    private final String TAG = "WishlistAdapter";
    private List<Wishlist> wishlistList;
    private LayoutInflater mInflater;

    public WishlistAdapter(Context context, List<Wishlist> wishlists) {
        super(context, 0, wishlists);
        this.mInflater = LayoutInflater.from(context);
        this.wishlistList = wishlists;
    }

    @Override
    public Wishlist getItem(int position) {
        return wishlistList.get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.wishlist_layout, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Wishlist item = getItem(position);

        assert item != null;
        Log.d(TAG, item.name);
        vh.textViewName.setText(item.name);
        return vh.rootView;
    }

    private static class ViewHolder {
        final RelativeLayout rootView;
        final TextView textViewName;

        private ViewHolder(RelativeLayout rootView, TextView textViewName) {
            this.rootView = rootView;
            this.textViewName = textViewName;
        }

        static ViewHolder create(RelativeLayout rootView) {
            TextView textViewName = rootView.findViewById(R.id.wishlistName);
            return new ViewHolder(rootView, textViewName);
        }
    }

}
