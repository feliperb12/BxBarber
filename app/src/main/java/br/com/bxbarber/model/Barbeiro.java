package br.com.bxbarber.model;

public class Barbeiro {
    private Integer foto;
    private String nomeBarbeiro;
    private String telefoneBarbeiro;

//    private String btn_perfil;

    public Barbeiro() {
    }

    public Barbeiro(Integer foto, String nomeBarbeiro, String telefoneBarbeiro) {
        this.foto = foto;
        this.nomeBarbeiro = nomeBarbeiro;
        this.telefoneBarbeiro = telefoneBarbeiro;
    }

    public Barbeiro( String nomeBarbeiro, String telefoneBarbeiro) {
        this.nomeBarbeiro = nomeBarbeiro;
        this.telefoneBarbeiro = telefoneBarbeiro;
    }


    public Integer getFoto() {
        return foto;
    }

    public void setFoto(Integer foto) {
        this.foto = foto;
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
