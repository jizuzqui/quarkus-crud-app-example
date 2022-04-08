package belfius.credit_risk.data.enrichment.dto;

import java.io.Serializable;

public class RowsAffected implements Serializable {

    private int rowsAffected;

    public RowsAffected() {
    }

    public RowsAffected(int rowsAffected) {
        this.rowsAffected = rowsAffected;
    }

    public int getRowsAffected() {
        return rowsAffected;
    }

    public void setRowsAffected(int rowsAffected) {
        this.rowsAffected = rowsAffected;
    }
    
}
