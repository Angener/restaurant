package epam.eremenko.restaurant.dto;

import java.io.Serializable;

public abstract class Dto implements Serializable {
    static final long serialVersionUID = 1L;
    private int id;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
