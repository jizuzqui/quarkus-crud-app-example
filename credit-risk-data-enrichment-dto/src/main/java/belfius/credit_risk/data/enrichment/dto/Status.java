package belfius.credit_risk.data.enrichment.dto;

import java.io.Serializable;

public class Status implements Serializable {

    private int status;

    public Status(){        
    }

    public Status(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}
