package kg.geektech.mokerapi;

import android.app.Application;

import kg.geektech.mokerapi.data.remote.HerokuApi;
import kg.geektech.mokerapi.data.remote.RetrofitClient;

public class App extends Application {

    public static RetrofitClient retrofitClient;
    public static HerokuApi api;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofitClient = new RetrofitClient();
        api = retrofitClient.provideApi();
    }
}
