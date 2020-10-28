package model;

public class Medicos {
    private String Nome;
    private String Especialidade;
    private String Hospital;
    private String Imagem;

    public Medicos() {
    }

    public Medicos(String nome, String especialidade, String hospital, String imagem) {
        Nome = nome;
        Especialidade = especialidade;
        Hospital = hospital;
        Imagem = imagem;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getEspecialidade() {
        return Especialidade;
    }

    public void setEspecialidade(String especialidade) {
        Especialidade = especialidade;
    }

    public String getHospital() {
        return Hospital;
    }

    public void setHospital(String hospital) {
        Hospital = hospital;
    }

    public String getImagem() {
        return Imagem;
    }

    public void setImagem(String imagem) {
        Imagem = imagem;
    }
}
