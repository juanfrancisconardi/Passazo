package nardi.francisco.juan.passazo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner = null;
    CodeScannerView scannerView = null;

    private ProgressDialog progressDialog;
    //private String URL_BASE = "http://192.168.1.10:8086/datasnap/rest/TServerMethods1/";//190.226.51.138"http://contadores.org.ar:8080/datasnap/rest/TFServerMetodos_1/";
    private String URL_BASE = "https://azopardoclub.ar/conectar/";
    private static String PREF_menu = "menu";
    private int PETICION_PERMISO_WIFI = 1;
    private int PETICION_PERMISO_CAMARA = 2;
    DbManager manager;
    List<ConsultaItem> arraylist_consulta;
    adapter_consulta adapter_consulta;
    ListView list_consulta;
    List<ConsultaItem> consulta_lista;
    TextView titulo;
    TextView cuerpo;
    EditText busquedaquery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissionsCAM();
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Herrajes Santa Fe");
        titulo = (TextView) findViewById(R.id.titulo);
        cuerpo = (TextView) findViewById(R.id.cuerpo);
        busquedaquery = (EditText) findViewById(R.id.busquedaquery);
       // busquedaquery.
        manager = new DbManager(this);
        busquedaquery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Buscar_consulta(busquedaquery.getText().toString());
                    return true;
                }else{
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Buscar_consulta(busquedaquery.getText().toString());
                    return true;
                }}

                    return false;
            }
        });
        list_consulta = (ListView) findViewById(R.id.list_consulta);
        list_consulta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Object listItem = list_consulta.getItemAtPosition(position); //agregar a consumos
                    ConsultaItem item = (ConsultaItem) parent.getAdapter().getItem(position);
             //   titulo.setText("Precio: $ " + String.format("%.2f",(Float.parseFloat(arraylist_consulta.get(0).getCantidad()) * 2.2f)));
                cuerpo.setText(arraylist_consulta.get(0).getObservaciones());
                list_consulta.setVisibility(View.GONE);
                busquedaquery.setText("");
        }});
    }
    public void click_buscar(View view) {
        Buscar_consulta(busquedaquery.getText().toString());
    }

public void click_qr(View view){

    if(mCodeScanner == null){
        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        scannerView.setVisibility(view.VISIBLE);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        busquedaquery.setText(result.getText());
                        Buscar_consulta(result.getText());
                      //  Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        scannerView.setVisibility(View.GONE);
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    SharedPreferences pref = getSharedPreferences("Passazo", MODE_PRIVATE);
    if(pref.getString("CAMARA", "0").equals("FRONT")){
        mCodeScanner.setCamera(CodeScanner.CAMERA_FRONT);
    }else{
        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK);
    }

    mCodeScanner.startPreview();
    }else{
        if(mCodeScanner.isPreviewActive()){
    scannerView.setVisibility(view.GONE);
        mCodeScanner.stopPreview();}else{
            scannerView.setVisibility(view.VISIBLE);
            mCodeScanner.startPreview();
        }
}
    }

    @Override
    protected void onResume() {
        super.onResume();
        busquedaquery.requestFocusFromTouch();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.salir:
                this.finishAffinity();
                this.finishAffinity();
                finish();
                return true;
            case R.id.reportar_error:
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"juan.nardi@gmail.com"});  //developer 's email
                Email.putExtra(Intent.EXTRA_SUBJECT,
                        "Reporte app Azopardo"); // Email 's Subject
                Email.putExtra(Intent.EXTRA_TEXT, "Detalle lo mejor posible la incidencia.");//FirebaseInstanceId.getInstance().getToken());  //Email 's Greeting text
                startActivity(Intent.createChooser(Email, "Enviar Comentario:"));
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.compartir:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "App Herrajes Santa Fe");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            case R.id.frontal:
                getApplicationContext().getSharedPreferences("Passazo", MODE_PRIVATE)
                        .edit()
                        .putString("CAMARA", "FRONT")
                        .commit();
                return true;
            case R.id.posterior:
                getApplicationContext().getSharedPreferences("Passazo", MODE_PRIVATE)
                        .edit()
                        .putString("CAMARA", "POSTERIOR")
                        .commit();
                return true;


            default:
                return false;
        }
    }


    private void Buscar_consulta(String cons) {
titulo.setText("");
cuerpo.setText("");
        if (manager.estaConectado()) {
            arraylist_consulta = new ArrayList<ConsultaItem>();



            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.setTitle("Consultando...");
            progressDialog.setMessage("Obteniendo Entradas...");
        //    progressDialog.show();
            JsonObjectRequest JsonObjectRequestRegistro;
            SharedPreferences pref = getSharedPreferences("Passazo", MODE_PRIVATE);

            JsonObjectRequestRegistro = new JsonObjectRequest(
                    Request.Method.GET,
                    URL_BASE + "cantidad.php?evento="+ pref.getString("evento", "no")+
                            "&fecha=" + pref.getString("fecha", "no") + "&qr_code=" + cons + "&cantidad="+ ((NumberPicker) findViewById(R.id.cantidad)).getValue(),
                    null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            if ((progressDialog != null) && progressDialog.isShowing())
                                progressDialog.dismiss();
                            try {
                                busquedaquery.setText("");
                                if (response.getJSONArray("result").getString(0).equals("incorrecto")) {
                                    Toast.makeText(getApplicationContext(), "Datos Incorrectos", Toast.LENGTH_LONG).show();
                                } else {
                                    try {

                                        if (!response.isNull("result") && !response.getJSONArray("result").get(0).equals("incorrecto") && !response.getJSONArray("result").getString(0).isEmpty()) {


                                            //FOREACH y cargar listview
                                            String st = response.getJSONArray("result").getString(0);

                                            String[] arreglo = st.split("@#@");//\\n");

                                            for (int i = 0; i < arreglo.length; i++) {

                                                try {
                                                    String respuesta = arreglo[i];
                                                    String[] separated = respuesta.split("#&#");

                                                    ConsultaItem Post = new ConsultaItem(separated[0],separated[1], separated[2],
                                                            separated[3],separated[4], separated[5],separated[6],separated[7],
                                                            separated[8],separated[9], separated[10]);
                                                    arraylist_consulta.add(Post);
                                                } catch (Exception e) {
                                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                                    if ((progressDialog != null) && progressDialog.isShowing())
                                                        progressDialog.dismiss();
                                                }
                                            }
                                            titulo.setText("Precio: $ " + arraylist_consulta.get(0).getEstado() + System.getProperty("line.separator"));
                                            cuerpo.setText(arraylist_consulta.get(0).getNombre() + " ( " + arraylist_consulta.get(0).getObservaciones() + " ) " );
                                            busquedaquery.setText("");
                                            try {
                                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                                r.play();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if ((progressDialog != null) && progressDialog.isShowing())
                                                progressDialog.dismiss();
                                        }
                                        if ((progressDialog != null) && progressDialog.isShowing())
                                            progressDialog.dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        if ((progressDialog != null) && progressDialog.isShowing())
                                            progressDialog.dismiss();
                                    }
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                                if ((progressDialog != null) && progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Problemas de comunicación...  ", Toast.LENGTH_LONG).show();
                            if ((progressDialog != null) && progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
            ) {

                //This is for Headers If You Needed
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String credentials = "juan" + ":" + "nardie40";
                    String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + base64EncodedCredentials);
                    return headers;
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("User", "juan");
                    params.put("Password", "nardie40");
                    return params;
                }
            };
            EventoSingleton.getInstance(this).addToRequestQueue(JsonObjectRequestRegistro);
        } else {
            Toast.makeText(this, "Usted no tiene conexión a internet",
                    Toast.LENGTH_LONG).show();
        }
    }
    private class adapter_consulta extends BaseAdapter {

        // Declare Variables

        Context mContext;
        LayoutInflater inflater;
        private List<ConsultaItem> ConsultaItemList = null;
        private ArrayList<ConsultaItem> arraylist;

        public adapter_consulta(Context context, List<ConsultaItem> ConsultaItemList) {
            mContext = context;
            this.ConsultaItemList = ConsultaItemList;
            inflater = LayoutInflater.from(mContext);
            this.arraylist = new ArrayList<ConsultaItem>();
            this.arraylist.addAll(ConsultaItemList);
        }

        public class ViewHolder {
            TextView name;
            ImageView icono;
        }

        @Override
        public int getCount() {
            return ConsultaItemList.size();
        }

        @Override
        public ConsultaItem getItem(int position) {
            return ConsultaItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.listview_item, null);
                // Locate the TextViews in listview_item.xml
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.icono = (ImageView) view.findViewById(R.id.icono);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            // Set the results into TextViews
            holder.name.setText(ConsultaItemList.get(position).getNombre());
         /*   if(ConsultaItemList.get(position).getCOCINA().equals("False")){
                holder.icono.setImageResource(R.drawable.cocktail);
            }else{
                holder.icono.setImageResource(R.drawable.comida);
            }
            */
            return view;
        }
    }

    private boolean checkPermissionsCAM() {

        int permissionCheck = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permissionCheck1 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED  ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PETICION_PERMISO_CAMARA);
        }

        if(permissionCheck1 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, PETICION_PERMISO_WIFI);
        }

        return true;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        //switch (requestCode) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PETICION_PERMISO_CAMARA || requestCode == PETICION_PERMISO_WIFI) { //case REQUEST_PERMISSION_CAMERA:
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Debes otorgar permiso de cámara!", Toast.LENGTH_SHORT).show();
            }
            //break;
            //case REQUEST_PERMISSION_WRITE:
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Debes otorgar el permiso de memoria", Toast.LENGTH_SHORT).show();
            }
            // break;
        }
    }

    public static List<String> extractUrls(String input)
    {
        List<String> result = new ArrayList<String>();

        String[] words = input.split("\\s+");


        Pattern pattern = Patterns.WEB_URL;
        for(String word : words)
        {
            if(pattern.matcher(word).find())
            {
                if(!word.toLowerCase().contains("http://") && !word.toLowerCase().contains("https://"))
                {
                    word = "http://" + word;
                }
                result.add(word);
            }
        }
        return result;
    }

    List<ConsultaItem> posts_clientes;
    private void Buscar_evento() {
        if (manager.estaConectado()) {
            posts_clientes = new ArrayList<ConsultaItem>();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.setTitle("Consultando...");
            progressDialog.setMessage("Obteniendo Eventos...");
            //    progressDialog.show();
            JsonObjectRequest JsonObjectRequestRegistro;

            JsonObjectRequestRegistro = new JsonObjectRequest(
                    Request.Method.GET,
                    URL_BASE + "/eventos.php",null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            if ((progressDialog != null) && progressDialog.isShowing())
                                progressDialog.dismiss();
                            try {

                                if (response.getJSONArray("result").getString(0).equals("incorrecto")) {
                                    Toast.makeText(getApplicationContext(), "Datos Incorrectos", Toast.LENGTH_LONG).show();
                                } else {
                                    try {

                                        if (!response.isNull("result") && !response.getJSONArray("result").get(0).equals("incorrecto") && !response.getJSONArray("result").getString(0).isEmpty()) {


                                            //FOREACH y cargar listview
                                            String st = response.getJSONArray("result").getString(0);

                                            String[] arreglo = st.split("@#@");//\\n");

                                            for (int i = 0; i < arreglo.length; i++) {

                                                try {
                                                    String respuesta = arreglo[i];
                                                    String[] separated = respuesta.split("#&#");

                                                    ConsultaItem Post = new ConsultaItem(separated[0],separated[1], separated[2],
                                                            separated[3],separated[4], separated[5],separated[6],separated[7],
                                                            separated[8],separated[9], separated[10]);
                                                    arraylist_consulta.add(Post);
                                                } catch (Exception e) {
                                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                                    if ((progressDialog != null) && progressDialog.isShowing())
                                                        progressDialog.dismiss();
                                                }
                                            }
                                            titulo.setText("Precio: $ " + arraylist_consulta.get(0).getEstado() + System.getProperty("line.separator"));
                                            cuerpo.setText(arraylist_consulta.get(0).getNombre() + " ( " + arraylist_consulta.get(0).getObservaciones() + " ) " );
                                            busquedaquery.setText("");

                                            try {
                                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                                r.play();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                                            builderSingle.setIcon(R.mipmap.ic_launcher);
                                            builderSingle.setTitle("Seleccionar Evento");

                                            clientes_adapter cli_adapter =  new clientes_adapter(MainActivity.this, posts_clientes);

                                            builderSingle.setAdapter(cli_adapter, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String codigo_cliente = cli_adapter.getItem(which).getDni();// .getItem(which);
                                                 //   tb.setText(codigo_cliente +" - " + cli_adapter.getItem(which).getNombre());
                                                    ActionBar actionBar = getSupportActionBar();
                                                    actionBar.setTitle(cli_adapter.getItem(which).getNombre());
                                                    actionBar.setSubtitle("CUIT : " + cli_adapter.getItem(which).getDni());
                                                }
                                            });
                                            builderSingle.show();


                                            if ((progressDialog != null) && progressDialog.isShowing())
                                                progressDialog.dismiss();

                                            if ((progressDialog != null) && progressDialog.isShowing())
                                                progressDialog.dismiss();
                                        }
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                                if ((progressDialog != null) && progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }
                    }    catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            if ((progressDialog != null) && progressDialog.isShowing())
                                progressDialog.dismiss();
                        }}},
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Problemas de comunicación...  ", Toast.LENGTH_LONG).show();
                            if ((progressDialog != null) && progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
            ) {

                //This is for Headers If You Needed
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String credentials = "juan" + ":" + "nardie40";
                    String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + base64EncodedCredentials);
                    return headers;
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("User", "juan");
                    params.put("Password", "nardie40");
                    return params;
                }
            };
            EventoSingleton.getInstance(this).addToRequestQueue(JsonObjectRequestRegistro);
        } else {
            Toast.makeText(this, "Usted no tiene conexión a internet",
                    Toast.LENGTH_LONG).show();
        }
    }




}