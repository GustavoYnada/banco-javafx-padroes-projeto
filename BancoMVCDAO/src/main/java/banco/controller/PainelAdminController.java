package banco.controller;

import banco.service.PainelAdminService;
import banco.sessao.Sessao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Locale;

public class PainelAdminController {

    @FXML private TextField txtCpf;
    @FXML private TextField txtValor;
    @FXML private TextField txtToken;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblMsg;
    @FXML private Hyperlink linkSair;

    private static final String BASE = "-fx-background-radius: 10; -fx-font-size: 12px; "
            + "-fx-font-weight: bold; -fx-padding: 8 12 8 12; ";
    private static final String ESTILO_ERRO = BASE + "-fx-background-color: #fdecec; -fx-text-fill: #c0392b;";
    private static final String ESTILO_SUCESSO = BASE + "-fx-background-color: #e8f7ee; -fx-text-fill: #1e8449;";

    @FXML
    private void initialize() {
        lblMsg.setVisible(false);
    }

    @FXML
    private void onAdicionarSaldo() {
        lblMsg.setVisible(false);

        String cpf = txtCpf.getText();
        double valor;
        String nomeDestino;
        try {
            valor = Double.parseDouble(txtValor.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            valor = 0;
        }
        try {
            PainelAdminService service = new PainelAdminService();
            nomeDestino = service.depositarSaldo(cpf, valor);
            mostrarMsg(String.format(Locale.of("pt", "BR"), "R$ %.2f adicionados a %s", valor, nomeDestino), true);
            txtCpf.clear();
            txtValor.clear();
        } catch (Exception e){
            mostrarMsg(e.getMessage(), false);
        }
    }

    @FXML
    private void onCriarAdmin() {
        lblMsg.setVisible(false);

        String token = txtToken.getText().trim();
        String senha = txtSenha.getText();

        try {
            PainelAdminService service = new PainelAdminService();
            service.cadastrarAdmin(token, senha);
            mostrarMsg("Administrador criado", true);
            txtToken.clear();
            txtSenha.clear();
        } catch (Exception e){
            mostrarMsg(e.getMessage(), false);
        }
    }

    @FXML
    private void onSair() {
        try {
            Sessao.limparSessao();
            Parent root = FXMLLoader.load(getClass().getResource("/banco/view/login-view.fxml"));
            Stage stage = (Stage) linkSair.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMsg("Não foi possível sair", false);
        }
    }

    private void mostrarMsg(String mensagem, boolean sucesso) {
        lblMsg.setText(mensagem);
        lblMsg.setStyle(sucesso ? ESTILO_SUCESSO : ESTILO_ERRO);
        lblMsg.setVisible(true);
    }
}