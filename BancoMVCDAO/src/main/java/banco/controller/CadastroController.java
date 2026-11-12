package banco.controller;

import banco.service.CadastroService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

public class CadastroController {

    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtConfirmaSenha;
    @FXML private ToggleButton tglVip;
    @FXML private Label lblMsg;
    @FXML private Button btnCadastrar;
    @FXML private Hyperlink linkVoltar;

    private static final String BASE = "-fx-background-radius: 10; -fx-font-size: 12px; "
            + "-fx-font-weight: bold; -fx-padding: 8 12 8 12; ";
    private static final String ESTILO_ERRO = BASE + "-fx-background-color: #fdecec; -fx-text-fill: #c0392b;";

    private static final String VIP_BASE = "-fx-background-insets: 0; -fx-background-radius: 10; -fx-border-radius: 10; "
            + "-fx-border-width: 1.5; -fx-padding: 11 14 11 14; -fx-font-size: 14px; -fx-font-weight: bold; "
            + "-fx-cursor: hand; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; ";
    private static final String VIP_OFF = VIP_BASE + "-fx-background-color: #f1f5f7; -fx-border-color: transparent; -fx-text-fill: #42535d;";
    private static final String VIP_ON = VIP_BASE + "-fx-background-color: #efe7fb; -fx-border-color: #6a3fb5; -fx-text-fill: #6a3fb5;";

    @FXML
    private void initialize() {
        esconderMsg();
        tglVip.setStyle(VIP_OFF);
        tglVip.selectedProperty().addListener((obs, antigo, ligado) -> {
            tglVip.setStyle(ligado ? VIP_ON : VIP_OFF);
        });
    }

    @FXML
    private void onCadastrar() {
        esconderMsg();

        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText();
        String senha = txtSenha.getText();
        String confirmacao = txtConfirmaSenha.getText();
        boolean isVip = tglVip.isSelected();

        try {
            CadastroService service = new CadastroService();
            service.cadastrarConta(nome, cpf, senha, confirmacao, isVip);
            onVoltar();
        } catch (Exception e) {
            mostrarMsg(e.getMessage());
        }
    }

    @FXML
    private void onVoltar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/banco/view/login-view.fxml"));
            Stage stage = (Stage) btnCadastrar.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMsg("Não foi possível voltar ao login");
        }
    }

    private void mostrarMsg(String mensagem) {
        lblMsg.setText(mensagem);
        lblMsg.setStyle(ESTILO_ERRO);
        lblMsg.setVisible(true);
        lblMsg.setManaged(true);
    }

    private void esconderMsg() {
        lblMsg.setVisible(false);
        lblMsg.setManaged(false);
    }
}