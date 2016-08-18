package alladsfeel.uapps.umidtech.com.alladsfeel;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    InterstitialAd mInterstitialAd;
    String testDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testDevice = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if( testDevice == null ) {
            Toast.makeText(MainActivity.this, "Error in fetching Device Id. Exit", Toast.LENGTH_SHORT).show();
            finish();
        }

        /* Step1: Initialize ads */
        MobileAds.initialize(getApplicationContext(),getString(R.string.app_id));

        /* Creating Banner add: Create adRequest and load into view */
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice(testDevice)  // An example device ID
                .build();

        mAdView.loadAd(adRequest);

        /* Creating Interstitial ad: */
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intertitial_ad_unid_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();

        Button btLaunch = (Button) findViewById(R.id.btLanuchAd);
        btLaunch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    Log.d(TAG,"Show InterstitialAd");
                    mInterstitialAd.show();
                }
            }
        });

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(testDevice)
                .build();
        mInterstitialAd.loadAd(adRequest);
        Log.d(TAG,"InterstitialAd loaded");
    }
}
