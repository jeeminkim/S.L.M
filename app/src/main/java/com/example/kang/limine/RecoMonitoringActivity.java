package com.example.kang.limine;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOBeaconRegionState;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECOMonitoringListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;


/**
 * RECOMonitoringActivity class is to monitor regions in the foreground.
 *
 * RECOMonitoringActivity 클래스는 foreground 상태에서 monitoring을 수행합니다.
 */
public class RecoMonitoringActivity extends RecoActivity implements RECOMonitoringListener, OnInitListener {
    private RecoMonitoringListAdapter mMonitoringListAdapter;
    private ListView mRegionListView;
    /**
     * We recommend 1 second for scanning, 10 seconds interval between scanning, and 60 seconds for region expiration time.
     * 1초 스캔, 10초 간격으로 스캔, 60초의 region expiration time은 당사 권장사항입니다.
     */

    private TextToSpeech myTTS;

    private long mScanPeriod = 1*1000L;
    private long mSleepPeriod = 10*1000L;
    private boolean mInitialSetting = true;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco_monitoring);


        //mRecoManager will be created here. (Refer to the RECOActivity.onCreate())
        //mRecoManager 인스턴스는 여기서 생성됩니다. RECOActivity.onCreate() 메소들르 참고하세요.
        //Set RECOMonitoringListener (Required)
        //RECOMonitoringListener 를 설정합니다. (필수)
        myTTS = new TextToSpeech(this, this);
        mRecoManager.setMonitoringListener(this);
        //Set scan period and sleep period.
        //The default is 1 second for the scan period and 10 seconds for the sleep period.
        mRecoManager.setScanPeriod(mScanPeriod);
        mRecoManager.setSleepPeriod(mSleepPeriod);
        /**
         * RECOServiceConnectListener와 함께 RECOBeaconManager를 bind 합니다. RECOServiceConnectListener는 RECOActivity에 구현되어 있습니다.
         * monitoring 및 ranging 기능을 사용하기 위해서는, 이 메소드가 "반드시" 호출되어야 합니다.
         * bind후에, onServiceConnect() 콜백 메소드가 호출됩니다. 콜백 메소드 호출 이후 monitoring / ranging 작업을 수행하시기 바랍니다.
         */
        mRecoManager.bind(this);




    }


    public void onInit(int status) {
        myTTS.speak("비콘 감지를 시작하겠습니다", TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMonitoringListAdapter = new RecoMonitoringListAdapter(this);
        mRegionListView = (ListView)findViewById(R.id.list_monitoring);
        mRegionListView.setAdapter(mMonitoringListAdapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stop(mRegions);
        this.unbind();

        myTTS.shutdown();
    }

    @Override
    public void onServiceConnect() {
        Log.i("RecoMonitoringActivity", "onServiceConnect");
        this.start(mRegions);
        //Write the code when RECOBeaconManager is bound to RECOBeaconService
    }

    @Override
    public void didDetermineStateForRegion(RECOBeaconRegionState recoRegionState, RECOBeaconRegion recoRegion) {
        Log.i("RecoMonitoringActivity", "didDetermineStateForRegion()");
        Log.i("RecoMonitoringActivity", "region: " + recoRegion.getUniqueIdentifier() + ", state: " + recoRegionState.toString());

        mMonitoringListAdapter.notifyDataSetChanged();
        //Write the code when the state of the monitored region is changed
    }

    @Override
    public void didEnterRegion(RECOBeaconRegion recoRegion, Collection<RECOBeacon> beacons) {
        /**
         * 최초 실행시, 이 콜백 메소드는 호출되지 않습니다.
         * didDetermineStateForRegion() 콜백 메소드를 통해 region 상태를 확인할 수 있습니다.
         */
        //Get the region and found beacon list in the entered region
        Log.i("RecoMonitoringActivity", "didEnterRegion() region:" + recoRegion.getUniqueIdentifier());
        mMonitoringListAdapter.updateRegion(recoRegion, RECOBeaconRegionState.RECOBeaconRegionInside, beacons.size(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(new Date()));
        mMonitoringListAdapter.notifyDataSetChanged();




        //Write the code when the device is enter the region

        if(recoRegion.getUniqueIdentifier().equals("식품관")) {
            Toast.makeText(this, "Enter 식품관", Toast.LENGTH_SHORT).show();
            myTTS.speak("식품관에 들어왔습니다", TextToSpeech.QUEUE_ADD, null);



        }




        else if(recoRegion.getUniqueIdentifier().equals("해산물")) {
            Toast.makeText(this, "Enter 해산물", Toast.LENGTH_SHORT).show();
            myTTS.speak("해산물 코너에 들어왔습니다", TextToSpeech.QUEUE_ADD, null);
        }
    }

    @Override
    public void didExitRegion(RECOBeaconRegion recoRegion) {
        /**
         * 최초 실행시, 이 콜백 메소드는 호출되지 않습니다.
         * didDetermineStateForRegion() 콜백 메소드를 통해 region 상태를 확인할 수 있습니다.
         */

        mMonitoringListAdapter.notifyDataSetChanged();

        if(recoRegion.getUniqueIdentifier().equals("식품관")) {
            Toast.makeText(this, "Exit 식품관", Toast.LENGTH_SHORT).show();
        }

        else if(recoRegion.getUniqueIdentifier().equals("해산물")) {
            Toast.makeText(this, "Exit 해산물", Toast.LENGTH_SHORT).show();
        }
        //Write the code when the device is exit the region
    }
    @Override
    public void didStartMonitoringForRegion(RECOBeaconRegion recoRegion) {
        Log.i("RecoMonitoringActivity", "didStartMonitoringForRegion: " + recoRegion.getUniqueIdentifier());
        //Write the code when starting monitoring the region is started successfully

        Toast.makeText(this, "monitoring succes", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void start(ArrayList<RECOBeaconRegion> regions) {
        Log.i("RecoMonitoringActivity", "start");
        for(RECOBeaconRegion region : regions) {
            try {
                region.setRegionExpirationTimeMillis(60*1000L);
                mRecoManager.startMonitoringForRegion(region);
            } catch (RemoteException e) {
                Log.i("RECOMonitoringActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RecoMonitoringActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void stop(ArrayList<RECOBeaconRegion> regions) {
        for(RECOBeaconRegion region : regions) {
            try {
                mRecoManager.stopMonitoringForRegion(region);
            } catch (RemoteException e) {
                Log.i("RecoMonitoringActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RecoMonitoringActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    private void unbind() {
        try {
            mRecoManager.unbind();
        } catch (RemoteException e) {
            Log.i("RecoMonitoringActivity", "Remote Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceFail(RECOErrorCode errorCode) {
        //Write the code when the RECOBeaconService is failed.
        //See the RECOErrorCode in the documents.
        Log.i("RECOMonitoringActivity", "onServiceFail");
        return;
    }


    @Override
    public void monitoringDidFailForRegion(RECOBeaconRegion region, RECOErrorCode errorCode) {
        //Write the code when the RECOBeaconService is failed to monitor the region.
        //See the RECOErrorCode in the documents.
        Toast.makeText(this, "monitoring fail", Toast.LENGTH_SHORT).show();
        return;
    }

    public void onButtonClicked(View v) {
        Button btn = (Button)v;
        if(btn.getId() == R.id.button_coupon) {
            final Intent intent = new Intent(this, Coupon.class);
            startActivity(intent);
        }
    }





}
