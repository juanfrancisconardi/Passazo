package nardi.francisco.juan.passazo;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DbManager {

    private Context contexto;
    int PERMISSION_READ_STATE = 1;

    public DbManager(Context context) {
        contexto = context;
    }

    protected Boolean estaConectado() {
        if (conectadoWifi()) {
            return true;
        }
                Toast.makeText(contexto, "Su Dispositivo no tiene Conexión a la Red...", Toast.LENGTH_LONG).show();
                return false;
    }

    public Boolean conectadoWifi() {
        ConnectivityManager connectivity = ((ConnectivityManager) contexto.getSystemService(contexto.CONNECTIVITY_SERVICE));
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }



    public String getMD5(final String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", "md5() NoSuchAlgorithmException: " + e.getMessage());
        }
        return "";
    }

    //---------------------------------------------------------------------------------------------



    /*        private static Account getAccount(AccountManager accountManager) {
                Account[] accounts = accountManager.getAccountsByType("com.google");
                Account account;
                if (accounts.length > 0) {
                    account = accounts[0];
                } else {
                    account = null;
                }
                return account;
            }*/

    /*
    public String getIMEI(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return "imei";
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
                return mngr.getImei();
            }

        }
        return "imei";


   *//*     if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            String imei = mngr != null ? mngr.getDeviceId() : null;
            return imei;
        }else{*//*

      //  }

     //return UUID.randomUUID().toString();
    }*/

    public String getMAC(Context context){
     /* WifiManager manager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String mac = info.getMacAddress().toString();
        return mac; */
        //UUID.randomUUID().toString();
        return "MAC";
    }

    public String getIP(Context context){
      /*  WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());*/
        return "IP";
    }
}





