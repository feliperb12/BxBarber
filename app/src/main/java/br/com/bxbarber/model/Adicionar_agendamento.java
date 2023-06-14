package br.com.bxbarber.model;

public class Adicionar_agendamento {
    private String data;
    private String servico;
    private String barbeiro;
    private String duracao;

    private Double valor;

    // Construtor vazio necessário para o Firebase Firestore
    public Adicionar_agendamento() {
    }

    public Adicionar_agendamento(String data, String servico, String barbeiro, String duracao, Double valor) {
        this.data = data;
        this.servico = servico;
        this.barbeiro = barbeiro;
        this.duracao = duracao;
        this.valor = valor;
    }

    public Adicionar_agendamento(String data, String servico, String barbeiro) {
        this.data = data;
        this.servico = servico;
        this.barbeiro = barbeiro;
    }

    public Adicionar_agendamento(String servico, String barbeiro) {
        this.data = data;
        this.servico = servico;
        this.barbeiro = barbeiro;
    }


    // Getters e setters
    // ...

    // toString() para exibição ou depuração
    @Override
    public String toString() {
        return "Adicionar_agendamento{" +
                "date='" + data + '\'' +
                ", service='" + servico + '\'' +
                ", barber='" + barbeiro + '\'' +
                ", duration='" + duracao + '\'' +
                ", valor='" + valor + '\'' +
                '}';
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getBarbeiro() {
        return barbeiro;
    }

    public void setBarbeiro(String barbeiro) {
        this.barbeiro = barbeiro;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
