package banco.model;

public class Admin {
    private String token, senha;
    public Admin(String token, String senha){
        this.token = token;
        this.senha = senha;
    }

    public boolean depositarConta(double valor, Conta c){
        return c.depositar(valor);
    }

    public String getToken() { return token; }
    public String getSenha() { return senha; }
}
