package com.rulzurlibrary.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulzurlibrary.R;
import com.rulzurlibrary.common.Notation;

import java.text.DecimalFormat;
import java.util.List;

public class NotationAdapter extends ArrayAdapter<Notation> {

        private List<Notation> notationList;
        private Context context;
        private LayoutInflater mInflater;

        // Constructors
        public NotationAdapter(Context context, List<Notation> objects) {
            super(context, 0, objects);
            this.context = context;
            this.mInflater = LayoutInflater.from(context);
            notationList = objects;
        }

        @Override
        public Notation getItem(int position) {
            return notationList.get(position);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            final com.rulzurlibrary.adapters.NotationAdapter.ViewHolder vh;
            if (convertView == null) {
                View view = mInflater.inflate(R.layout.notation_layout, parent, false);
                vh = com.rulzurlibrary.adapters.NotationAdapter.ViewHolder.create((RelativeLayout) view);
                view.setTag(vh);
            } else {
                vh = (com.rulzurlibrary.adapters.NotationAdapter.ViewHolder) convertView.getTag();
            }

            Notation item = getItem(position);

            assert item != null;

            vh.notationText.setText(String.format("%s: %s/%d", item.provider, new DecimalFormat("#.##").format(item.note), item.max));
            return vh.rootView;
        }

        private static class ViewHolder {
            final RelativeLayout rootView;
            final TextView notationText;

            private ViewHolder(RelativeLayout rootView, TextView notationText) {
                this.rootView = rootView;
                this.notationText = notationText;
            }

            static com.rulzurlibrary.adapters.NotationAdapter.ViewHolder create(RelativeLayout rootView) {
                TextView notationText = rootView.findViewById(R.id.notationText);
                return new com.rulzurlibrary.adapters.NotationAdapter.ViewHolder(rootView, notationText);
            }
        }

}
