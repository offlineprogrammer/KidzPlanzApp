package com.offlineprogrammer.kidzplanz.plan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KidPlan implements Parcelable {
    private String planName;
    private String planImageResourceName;
    private Date createdDate;
    private String firestoreId;
    private String kidFirestoreId;

    public KidPlan(String planName, String planImageResourceName, Date createdDate) {
        this.planName = planName;
        this.planImageResourceName = planImageResourceName;
        this.createdDate = createdDate;

    }

    protected KidPlan(Parcel in) {
        planName = in.readString();
        planImageResourceName = in.readString();
        firestoreId = in.readString();
        kidFirestoreId = in.readString();
    }

    public KidPlan(){

    }

    public static final Creator<KidPlan> CREATOR = new Creator<KidPlan>() {
        @Override
        public KidPlan createFromParcel(Parcel in) {
            return new KidPlan(in);
        }

        @Override
        public KidPlan[] newArray(int size) {
            return new KidPlan[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(planName);
        dest.writeString(planImageResourceName);
        dest.writeString(firestoreId);
        dest.writeString(kidFirestoreId);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("planName", this.planName);
        result.put("planImageResourceName", this.planImageResourceName);
        result.put("createdDate", this.createdDate);
        result.put("firestoreId", this.firestoreId);
        result.put("kidFirestoreId", this.kidFirestoreId);
        return result;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanImageResourceName() {
        return planImageResourceName;
    }

    public void setPlanImageResourceName(String planImageResourceName) {
        this.planImageResourceName = planImageResourceName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public void setFirestoreId(String firestoreId) {
        this.firestoreId = firestoreId;
    }

    public String getKidFirestoreId() {
        return kidFirestoreId;
    }

    public void setKidFirestoreId(String kidFirestoreId) {
        this.kidFirestoreId = kidFirestoreId;
    }
}
