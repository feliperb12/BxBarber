package br.com.bxbarber;

public class Usuarios {

    String usuarioId, nome, perfil;

    public Usuarios(String usuarioId, String nome, String perfil) {
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.perfil = perfil;
    }

    public Usuarios() {
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}
