package com.jramdev.bandssocialnetwork;

import com.google.firebase.database.PropertyName;

public class Chat {
    String mensaje,receptor,emisor,timeStamp;
    boolean isSeen;

    public Chat() {
    }

    public Chat(String mensaje, String receptor, String emisor, String timeStamp, boolean isSeen) {
        this.mensaje = mensaje;
        this.receptor = receptor;
        this.emisor = emisor;
        this.timeStamp = timeStamp;
        this.isSeen = isSeen;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    @PropertyName("isSeen")
    public boolean isSeen() {
        return isSeen;
    }
    @PropertyName("isSeen")
    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
