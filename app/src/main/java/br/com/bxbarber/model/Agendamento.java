package br.com.bxbarber.model;
import android.os.Parcel;
import android.os.Parcelable;
public class Agendamento implements Parcelable {
    private String barbeiro;
    private String data;
    private String duracao;
    private String servico;
    private String id;
    // Restante dos atributos e métodos

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Agendamento() {
        // Construtor vazio necessário para o Firestore
    }

    protected Agendamento(Parcel in) {
        barbeiro = in.readString();
        data = in.readString();
        duracao = in.readString();
        servico = in.readString();
    }

    public static final Creator<Agendamento> CREATOR = new Creator<Agendamento>() {
        @Override
        public Agendamento createFromParcel(Parcel in) {
            return new Agendamento(in);
        }

        @Override
        public Agendamento[] newArray(int size) {
            return new Agendamento[size];
        }
    };

    public String getBarbeiro() {
        return barbeiro;
    }

    public void setBarbeiro(String barbeiro) {
        this.barbeiro = barbeiro;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    // Implemente os métodos Parcelable aqui

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(barbeiro);
        dest.writeString(data);
        dest.writeString(duracao);
        dest.writeString(servico);
    }
}