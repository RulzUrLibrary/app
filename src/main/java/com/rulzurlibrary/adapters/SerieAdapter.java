package com.rulzurlibrary.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulzurlibrary.R;
import com.rulzurlibrary.common.Serie;
import com.squareup.picasso.Picasso;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.series_layout, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Serie item = getItem(position);

        assert item != null;
        vh.textViewName.setText(item.title());

        if (!item.isBook()) {
            vh.textViewEmail.setText(item.volumes());
            vh.textViewEmail.setBackgroundColor(context.getColor(item.ratio() == 1.0 ? R.color.success : R.color.primary));
        }
        Picasso p = Picasso.with(context);
        //p.setLoggingEnabled(true);
        p.load(item.getThumbName()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        final RelativeLayout rootView;
        final ImageView imageView;
        final TextView textViewName;
        final TextView textViewEmail;

        private ViewHolder(RelativeLayout rootView) {
            this.rootView = rootView;
            this.imageView = rootView.findViewById(R.id.imageView);
            this.textViewName = rootView.findViewById(R.id.textViewName);
            this.textViewEmail = rootView.findViewById(R.id.textViewEmail);
        }

        static ViewHolder create(RelativeLayout rootView) {
            return new ViewHolder(rootView);
        }
    }
}
