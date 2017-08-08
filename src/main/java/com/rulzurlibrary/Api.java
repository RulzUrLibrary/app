package com.rulzurlibrary;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 8/8/17.
 */

public class Api {
    private static Api mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private static final String url = "http://rulz.xyz";

    private Api(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }


    public static synchronized Api getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Api(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void sendIsbn(String isbn) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("isbn", isbn);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url + "/books", data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("foo", response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("foo", "Error: " + error.getMessage());

            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        Log.d("foo", "SENDING ISBN MOTHERFUUUUCKER!");
        getRequestQueue().add(jsonObjReq);
    }
}
