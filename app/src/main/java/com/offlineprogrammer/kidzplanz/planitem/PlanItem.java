package com.offlineprogrammer.kidzplanz.planitem;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlanItem {
    private String planItemName;
    private Date createdDate;
    private Boolean bCompleted;
    private String firestoreId;
    private String planFirestoreId;

    public PlanItem(String planItemName, Date createdDate, Boolean bCompleted) {
        this.planItemName = planItemName;
        this.createdDate = createdDate;
        this.bCompleted = bCompleted;
    }

    public PlanItem(){

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("planItemName", this.planItemName);
        result.put("bCompleted", this.bCompleted);
        result.put("createdDate", this.createdDate);
        result.put("firestoreId", this.firestoreId);
        result.put("planFirestoreId", this.planFirestoreId);
        return result;
    }

    public String getPlanItemName() {
        return planItemName;
    }

    public void setPlanItemName(String planItemName) {
        this.planItemName = planItemName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getbCompleted() {
        return bCompleted;
    }

    public void setbCompleted(Boolean bCompleted) {
        this.bCompleted = bCompleted;
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public void setFirestoreId(String firestoreId) {
        this.firestoreId = firestoreId;
    }

    public String getPlanFirestoreId() {
        return planFirestoreId;
    }

    public void setPlanFirestoreId(String planFirestoreId) {
        this.planFirestoreId = planFirestoreId;
    }
}
