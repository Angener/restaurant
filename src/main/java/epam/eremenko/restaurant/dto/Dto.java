package epam.eremenko.restaurant.dto;

import java.io.Serializable;

public abstract class Dto implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
