package com.rulzurlibrary;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rulzurlibrary.common.Book;
import com.rulzurlibrary.common.RulzUrLibraryService;
import com.rulzurlibrary.common.Wishlist;
import com.rulzurlibrary.common.Wishlists;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistActivity extends AppCompatActivity {
    private final String TAG = "WishlistActivity";
    private Wishlists wishlists;
    private ListView wishlistsCheckboxes;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlists);
        setActionBar();

        book = getIntent().getParcelableExtra("book");
        wishlistsCheckboxes = (ListView) findViewById(R.id.wishlistsCheckboxes);

        RulzUrLibraryService.client.getWishlists().enqueue(new Callback<Wishlists>() {
            @Override
            public void onResponse(@NonNull Call<Wishlists> call, @NonNull Response<Wishlists> response) {
                if (response.isSuccessful()) {
                    // user object available
                    wishlists = response.body();
                    wishlistsCheckboxes.setAdapter(
                            new WishlistActivity.WishlistAdapter(WishlistActivity.this, wishlists.wishlists)
                    );
                } else {
                    Log.e(TAG, response.toString());
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(@NonNull Call<Wishlists> call, @NonNull Throwable t) {
                // something went completely south (like no internet connection)
                Log.e(TAG, t.getMessage());
            }
        });

    }

    public void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
    }

    private class WishlistAdapter extends ArrayAdapter<Wishlist> {

        private List<Wishlist> wishlists;
        private LayoutInflater mInflater;

        WishlistAdapter(Context context, List<Wishlist> objects) {
            super(context, 0, objects);
            this.mInflater = LayoutInflater.from(context);
            this.wishlists = objects;
        }

        @Override
        public Wishlist getItem(int position) {
            return wishlists.get(position);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            final WishlistActivity.ViewHolder vh;

            if (convertView == null) {
                View view = mInflater.inflate(R.layout.wishlist_checkbox_layout, parent, false);
                vh = WishlistActivity.ViewHolder.create((RelativeLayout) view);
                view.setTag(vh);
            } else {
                vh = (WishlistActivity.ViewHolder) convertView.getTag();
            }
            Wishlist item = getItem(position);

            assert item != null;
            vh.wishlistCheckBox.setText(item.name);
            return vh.rootView;
        }
    }

    private static class ViewHolder {
        final RelativeLayout rootView;
        final CheckBox wishlistCheckBox;

        private ViewHolder(RelativeLayout rootView) {
            this.rootView = rootView;
            this.wishlistCheckBox = rootView.findViewById(R.id.wishlistCheckBox);
        }

        static ViewHolder create(RelativeLayout rootView) {
            return new ViewHolder(rootView);
        }
    }
}
