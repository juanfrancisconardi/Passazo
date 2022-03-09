package nardi.francisco.juan.passazo;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public final class EventoSingleton {
    // Atributos
    private static EventoSingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;

    private EventoSingleton(Context context) {
        EventoSingleton.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized EventoSingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new EventoSingleton(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void addToRequestQueue(Request req) {

        getRequestQueue().add(req);
    }

}