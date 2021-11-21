package com.dassoop.awsec2utility.models;

public class Instance
{
    private String name;
    private String publicIP4v;
    private String publicIP4vDNS;
    private String keypair;

    public Instance()
    {

    }

    public Instance(String name, String publicIP4v, String publicIP4vDNS, String keypair)
    {
        this.name = name;
        this.publicIP4v = publicIP4v;
        this.publicIP4vDNS = publicIP4vDNS;
        this.keypair = keypair;
    }

//|========================GETTERS AND SETTERS ========================|
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicIP4v() {
        return publicIP4v;
    }

    public void setPublicIP4v(String publicIP4v) {
        this.publicIP4v = publicIP4v;
    }

    public String getPublicIP4vDNS() {
        return publicIP4vDNS;
    }

    public void setPublicIP4vDNS(String publicIP4vDNS) {
        this.publicIP4vDNS = publicIP4vDNS;
    }

    public String getKeypair() {
        return keypair;
    }

    public void setKeypair(String keypair) {
        this.keypair = keypair;
    }
}
