package com.rulzurlibrary;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.os.Handler;
import android.os.Message;
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

    private Handler mHandler;
    private final PostResponse mPostResponse = new PostResponse();

    private Api(Context context, Handler handler) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        mHandler = handler;
    }


    public static synchronized Api getInstance(Context context, Handler handler) {
        if (mInstance == null) {
            mInstance = new Api(context, handler);
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

    public void sendIsbn(String isbn) {
        JSONObject data = new JSONObject();
        try {
            data.put("isbn", isbn);
        } catch (JSONException e) {
            e.printStackTrace(); // this should not happen
        }

        Log.d("foo", "SENDING ISBN "+isbn);
        getRequestQueue().add(new PostRequest(Request.Method.POST, data));
    }
    private class PostRequest extends JsonObjectRequest {

        public PostRequest(int method, JSONObject jsonRequest) {
            super(method, "http://rulz.xyz/books", jsonRequest, mPostResponse, mPostResponse);
        }
        /**
         * Passing some request headers
         * */
        @Override
        public Map<String, String> getHeaders() {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json");
            return headers;
        }
    }

    private class PostResponse  implements Response.Listener<JSONObject>, Response.ErrorListener {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("foo", response.toString());
            Message msg = Message.obtain(); // Creates an new Message instance
            try {
                msg.obj =  new Book(
                        response.getString("isbn"), response.getString("title"),
                        response.getString("description")
                );
            } catch (JSONException e) {
                Log.e("foo", "impossible to unmarshal JSON", e);
                e.printStackTrace();
            };// Put the string into Message, into "obj" field.
            msg.setTarget(mHandler); // Set the Handler
            msg.sendToTarget(); //Send the message

        }

        @Override
        public void onErrorResponse(VolleyError error) {
            VolleyLog.d("foo", "Error: " + error.getMessage());
        }
    }
}
