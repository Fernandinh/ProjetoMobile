package model;

public class Vacinas {

    private String Nome;
    private String Descricao;
    private String Indicacao;
    private String Imagem;

    public Vacinas() {
    }

    public Vacinas(String nome, String descricao, String indicacao, String imagem) {
        Nome = nome;
        Descricao = descricao;
        Indicacao = indicacao;
        Imagem = imagem;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getIndicacao() {
        return Indicacao;
    }

    public void setIndicacao(String indicacao) {
        Indicacao = indicacao;
    }

    public String getImagem() {
        return Imagem;
    }

    public void setImagem(String imagem) {
        Imagem = imagem;
    }
}
