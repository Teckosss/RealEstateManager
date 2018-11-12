package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils extends AsyncTask<Void,Void,Boolean> {

    private static int timeOutMs = 1500;
    private static String googleDNS = "8.8.8.8";
    private static int googleDNSPort = 53;

    private Consumer mConsumer;
    public  interface Consumer { void accept(Boolean internet); }

    public  Utils(Consumer consumer) { mConsumer = consumer; execute(); }

    @Override protected Boolean doInBackground(Void... voids) {
        try {
        Socket sock = new Socket();
        sock.connect(new InetSocketAddress(googleDNS, googleDNSPort), timeOutMs);
        sock.close();
        return true;
    } catch (IOException e) {
            return false;
        }
    }

    @Override protected void onPostExecute(Boolean internet) { mConsumer.accept(internet); }

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }

    public static int convertEuroToDollar(int euro){ return (int) Math.round(euro * 1.142);}

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Single<Boolean> isInternetAvailable(Context context){
       Single.fromCallable(() -> {
           ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

           NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
           boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

           if (isConnected){
               try{
                   int timeOutMs = 1500;
                   Socket socket = new Socket();
                   InetSocketAddress socketAddress = new InetSocketAddress(googleDNS, googleDNSPort);

                   socket.connect(socketAddress, timeOutMs);
                   socket.close();
                   return true;
               }catch (IOException e){
                   Log.e("UTILS_Internet","isInternetAvailable Exception : " + e);
                   return false;
               }
           }
          return false;
       })
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread());
        return null;
    }
}

