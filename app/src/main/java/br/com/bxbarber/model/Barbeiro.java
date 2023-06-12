package br.com.bxbarber.model;

public class Barbeiro {
    private String imagemUrl;
    private String nomeBarbeiro;
    private String telefoneBarbeiro;

    public Barbeiro() {
    }

    public Barbeiro(String nomeBarbeiro, String telefoneBarbeiro, String imagemUrl) {
        this.imagemUrl = imagemUrl;
        this.nomeBarbeiro = nomeBarbeiro;
        this.telefoneBarbeiro = telefoneBarbeiro;
    }

    public Barbeiro(String nomeBarbeiro, String telefoneBarbeiro) {
        this.nomeBarbeiro = nomeBarbeiro;
        this.telefoneBarbeiro = telefoneBarbeiro;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public String getNomeBarbeiro() {
        return nomeBarbeiro;
    }

    public void setNomeBarbeiro(String nomeBarbeiro) {
        this.nomeBarbeiro = nomeBarbeiro;
    }

    public String getTelefoneBarbeiro() {
        return telefoneBarbeiro;
    }

    public void setTelefoneBarbeiro(String telefoneBarbeiro) {
        this.telefoneBarbeiro = telefoneBarbeiro;
    }
}
