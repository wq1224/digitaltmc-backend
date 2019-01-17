package org.tmc.digitaltmc.modal;
import lombok.Data;

@Data public class User{

    String name;
    String openid;

    @Override
    public String toString(){
        return name;
    }
}