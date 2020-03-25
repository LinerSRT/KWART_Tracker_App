package com.liner.familytracker.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;

public final class GSMState {
    private Context context;
    private int mcc; //Country code
    private int mnc; //Operator code
    private int cellid; // Cell id
    private int lac; // Region code or Area location

    public GSMState(final Context context) {
        this.context = context;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if ((telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) || (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA)) {
                if (ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                final CellLocation location = telephonyManager.getCellLocation();
                        for(CellInfo cellInfo:telephonyManager.getAllCellInfo()){
                            if (cellInfo instanceof CellInfoGsm){
                                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                                Log.d("TAGTAG", "\n" +
                                        "mcc: "+cellIdentity.getMcc()+"\n" +
                                        "mnc: "+cellIdentity.getMnc()+"\n" +
                                        "cellid: "+cellIdentity.getCid()+"\n" +
                                        "lac: "+cellIdentity.getLac()+"\n");
                            }
                        }
                if (location != null) {
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) location;
                    setCellid(gsmCellLocation.getCid());
                    setLac(gsmCellLocation.getLac());
                    String networkOperator = telephonyManager.getNetworkOperator();
                    if (networkOperator != null && networkOperator.length() >= 3) {
                        setMcc(Integer.parseInt(networkOperator.substring(0, 3)));
                        setMnc(Integer.parseInt(networkOperator.substring(3)));
                    }
                }
            }

        }
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getCellid() {
        return cellid;
    }

    public void setCellid(int cellid) {
        this.cellid = cellid;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    @Override
    public String toString() {
        return "GSMState{" +
                "mcc=" + mcc +
                ", mnc=" + mnc +
                ", cellid=" + cellid +
                ", lac=" + lac +
                '}';
    }
}