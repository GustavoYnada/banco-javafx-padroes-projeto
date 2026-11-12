package banco.controller;

import banco.service.LoginService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtCpf;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblErro;
    @FXML private Button btnEntrar;

    @FXML
    private void initialize() {
        esconderErro();
    }

    @FXML
    private void onEntrar() {
        esconderErro();
        String login = txtCpf.getText().trim();
        String senha = txtSenha.getText();

        try {
            LoginService service = new LoginService();
            String tela = service.autenticarUsuario(login, senha);
            switch (tela){
                case "NORMAL": trocarTela("/banco/view/painel-normal-view.fxml"); break;
                case "VIP": trocarTela("/banco/view/painel-vip-view.fxml"); break;
                case "ADM": trocarTela("/banco/view/painel-admin-view.fxml"); break;
            }
        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void onCadastro() {
        try {
            trocarTela("/banco/view/cadastro-view.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir a tela");
        }
    }

    private void trocarTela(String caminhoFxml) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(caminhoFxml));
        Stage stage = (Stage) btnEntrar.getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    private void mostrarErro(String mensagem) {
        lblErro.setText(mensagem);
        lblErro.setVisible(true);
        lblErro.setManaged(true);
    }

    private void esconderErro() {
        lblErro.setVisible(false);
        lblErro.setManaged(false);
    }
}