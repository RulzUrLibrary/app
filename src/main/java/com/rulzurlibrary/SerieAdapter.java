package com.rulzurlibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulzurlibrary.common.Serie;

import java.util.List;

public class SerieAdapter extends ArrayAdapter<Serie> {

    private List<Serie> serieList;
    private Context context;
    private LayoutInflater mInflater;

    // Constructors
    public SerieAdapter(Context context, List<Serie> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        serieList = objects;
    }

    @Override
    public Serie getItem(int position) {
        return serieList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.serie, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Serie item = getItem(position);

        assert item != null;
        vh.textViewName.setText(item.Title());
        //vh.textViewEmail.setText(item.getEmail());
        //Picasso.with(context).load(item.getProfilePic()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        final RelativeLayout rootView;
        final ImageView imageView;
        final TextView textViewName;
        final TextView textViewEmail;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewName, TextView textViewEmail) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewName = textViewName;
            this.textViewEmail = textViewEmail;
        }

        static ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = rootView.findViewById(R.id.imageView);
            TextView textViewName = rootView.findViewById(R.id.textViewName);
            TextView textViewEmail = rootView.findViewById(R.id.textViewEmail);
            return new ViewHolder(rootView, imageView, textViewName, textViewEmail);
        }
    }
}
