package com.gruposalinas.elektra.movilidadgs.utils;

import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by oemy9 on 06/03/2017.
 */

public abstract class EncryptBase {
    protected KeyGenerator kg;
    protected SecretKeySpec sks=null;
    protected   SecureRandom sr;
    private String fechaSeed;
    public String getFechaSeed() {
        return fechaSeed;
    }
    public void setFechaSeed(String fechaSeed) {
        this.fechaSeed = fechaSeed;
    }
    public EncryptBase(String fechaSeed){
            this.fechaSeed = fechaSeed;
    }
    public abstract byte[]encriptar();
    public abstract byte[]desencriptar();
    public abstract String encripToString();
}
