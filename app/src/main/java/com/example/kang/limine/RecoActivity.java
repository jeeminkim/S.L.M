package com.example.kang.limine;
import android.app.Activity;
import android.os.Bundle;

import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOServiceConnectListener;

import java.util.ArrayList;

/**
 * RECOActivity class is the base activity for RECOMonitoringActivity and RECORangingActivity.
 * If you want to implement monitoring or ranging in a single class,
 * you can remove this class and include the methods and RECOServiceConnectListener to each class.
 *
 * RECOActivity 클래스는 RECOMonitoringActivity와 RECORangingActivity를 위한 기본 클래스 입니다.
 * Monitoring 이나 ranging을 단일 클래스로 구성하고 싶으시다면, 이 클래스를 삭제하시고 필요한 메소드와 RECOServiceConnectListener를 해당 클래스에서 구현하시기 바랍니다.
 */
public abstract class RecoActivity extends Activity implements RECOServiceConnectListener {
    protected RECOBeaconManager mRecoManager;
    protected ArrayList<RECOBeaconRegion> mRegions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * RECOBeaconManager 인스턴스틀 생성합니다. (스캔 대상 및 백그라운드 ranging timeout 설정)
         * RECO만을 스캔하고, 백그라운드 ranging timeout을 설정하고 싶지 않으시다면, 다음과 같이 생성하시기 바랍니다.
         * 		mRecoManager = RECOBeaconManager.getInstance(getApplicationContext(), true, false);
         * 주의: enableRangingTimeout을 false로 설정 시, 배터리 소모량이 증가합니다.
         */
        mRecoManager = RECOBeaconManager.getInstance(getApplicationContext(), BeaconMainActivity.SCAN_RECO_ONLY, BeaconMainActivity.ENABLE_BACKGROUND_RANGING_TIMEOUT);
        mRegions = this.generateBeaconRegion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ArrayList<RECOBeaconRegion> generateBeaconRegion() {
        ArrayList<RECOBeaconRegion> regions = new ArrayList<RECOBeaconRegion>();
        int mMajor = 501;


        int mMinor1 = 716;
        int mMinor2 = 717;
        int mMinor3 = 718;


        RECOBeaconRegion mRecoRegion1 = new RECOBeaconRegion(BeaconMainActivity.RECO_UUID, mMajor, mMinor1, "육류");
        RECOBeaconRegion mRecoRegion2= new RECOBeaconRegion(BeaconMainActivity.RECO_UUID, mMajor, mMinor2, "해산물류");
        RECOBeaconRegion mRecoRegion3 = new RECOBeaconRegion(BeaconMainActivity.RECO_UUID, mMajor, mMinor3, "비식품");


        regions.add(mRecoRegion1);
        regions.add(mRecoRegion2);
        regions.add(mRecoRegion3);

        return regions;
    }

    protected abstract void start(ArrayList<RECOBeaconRegion> regions);
    protected abstract void stop(ArrayList<RECOBeaconRegion> regions);
}
