package model;

public class Usuario {

    private String nome;
    private String email;
    private String imagem;
    private String senha;
    private String dtsnc;
    private String cpf;
    private  String uid;

    public Usuario() {

    }

    public Usuario(String nome, String email, String imagem, String senha, String dtsnc, String cpf, String uid) {
        this.nome = nome;
        this.email = email;
        this.imagem = imagem;
        this.senha = senha;
        this.dtsnc = dtsnc;
        this.cpf = cpf;
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDtsnc() {
        return dtsnc;
    }

    public void setDtsnc(String dtsnc) {
        this.dtsnc = dtsnc;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
