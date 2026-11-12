package banco.controller;

import banco.observer.ContaObserver;
import banco.service.ContaService;
import banco.service.TransacaoService;
import banco.sessao.Sessao;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

public class PainelVipController implements ContaObserver {

    @FXML private Label lblNome;
    @FXML private Label lblSaldo;
    @FXML private Label lblFatura;
    @FXML private Label lblLimite;
    @FXML private Label lblMsg;
    @FXML private Hyperlink linkSair;
    @FXML private Button btnRetirar;

    private static final String BASE = "-fx-background-radius: 10; -fx-font-size: 12px; "
            + "-fx-font-weight: bold; -fx-padding: 8 12 8 12; ";
    private static final String ESTILO_ERRO = BASE + "-fx-background-color: #fdecec; -fx-text-fill: #c0392b;";
    private static final String ESTILO_SUCESSO = BASE + "-fx-background-color: #e8f7ee; -fx-text-fill: #1e8449;";

    private static final String ESTILO_CAMPO = "-fx-background-color: #f1f5f7; -fx-background-radius: 10; "
            + "-fx-padding: 11 14 11 14; -fx-font-size: 14px;";

    @FXML
    private void initialize() {
        esconderMsg();
        Sessao.adicionarObserver(this);
        atualizarTela();
    }

    public void contaAtualizada() {
        atualizarTela();
    }

    @FXML
    private void onRetirar() {
        esconderMsg();
        Optional<String[]> dados = pedirDados("Retirar", "Quanto você quer retirar?", false);
        if (dados.isEmpty()) return;

        double valor = lerValor(dados.get()[0]);

        try {
            TransacaoService service = new TransacaoService();
            service.validarSaque(valor);
            if (!confirmar("Confirmar retirada", "Retirar " + moeda(valor) + " da sua conta?")) return;
            service.fazerSaque(valor);
            mostrarSucesso("Retirada de " + moeda(valor) + " realizada");
        } catch (Exception e){
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void onTransferir() {
        esconderMsg();
        Optional<String[]> dados = pedirDados("Transferir", "Para quem e quanto?", true);
        if (dados.isEmpty()) return;

        double valor = lerValor(dados.get()[0]);
        String cpfDestino = dados.get()[1];

        try {
            TransacaoService service = new TransacaoService();
            String nomeDestino = service.validarTransferencia(valor, cpfDestino);

            if (!confirmar("Confirmar transferência", "Transferir " + moeda(valor) + " para\n"
                    + nomeDestino + " (CPF " + cpfDestino + ")?")) return;

            service.fazerTransferencia(valor, cpfDestino);
            mostrarSucesso("Transferência de " + moeda(valor) + " realizada");

        } catch (Exception e){
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void onPagarFatura() {
        esconderMsg();
        try{
            ContaService contaService = new ContaService();
            contaService.faturaZerada();
        } catch (Exception e){
            mostrarErro(e.getMessage());
            return;
        }

        Optional<String[]> dados = pedirDados("Pagar fatura",
                "Fatura atual: " + moeda(Sessao.getContaVIP().getFaturaCredito()), false);
        if (dados.isEmpty()) return;
        double valor = lerValor(dados.get()[0]);

        try{
            TransacaoService service = new TransacaoService();
            double pago = service.validarFatura(valor);
            if (!confirmar("Confirmar pagamento", "Pagar " + moeda(pago) + " da fatura de crédito?")) return;
            service.pagarFatura(valor);

            mostrarSucesso("Fatura paga: " + moeda(pago));

        } catch (Exception e){
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void onExcluirConta() {
        esconderMsg();

        try {
            ContaService contaService = new ContaService();
            contaService.faturaPaga();

            if (!confirmar("Excluir conta", "Isso apaga sua conta de forma definitiva.\n"
                    + "Saldo perdido: " + moeda(Sessao.getContaVIP().getSaldo()))) return;
            String senha = pedirSenha();
            if (senha == null) return;

            contaService.excluirConta(senha);
            onSair();
        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void onSair() {
        try {
            Sessao.removerObserver(this);
            Sessao.limparSessao();
            Parent root = FXMLLoader.load(getClass().getResource("/banco/view/login-view.fxml"));
            Stage stage = (Stage) linkSair.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Não foi possível sair");
        }
    }

    private void atualizarTela() {
        lblNome.setText(Sessao.getContaVIP().getNome());
        lblSaldo.setText(moeda(Sessao.getContaVIP().getSaldo()));
        lblFatura.setText(moeda(Sessao.getContaVIP().getFaturaCredito()));
        lblLimite.setText("Limite: " + moeda(Sessao.getContaVIP().getLimiteCredito()));
    }

    private Optional<String[]> pedirDados(String titulo, String cabecalho, boolean pedirCpf) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.initOwner(btnRetirar.getScene().getWindow());
        dialog.setTitle(titulo);
        dialog.setHeaderText(cabecalho);

        ButtonType confirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmar, ButtonType.CANCEL);

        TextField txtValor = new TextField();
        txtValor.setPromptText("0,00");
        txtValor.setStyle(ESTILO_CAMPO);

        TextField txtCpf = new TextField();
        txtCpf.setPromptText("000.000.000-00");
        txtCpf.setStyle(ESTILO_CAMPO);

        VBox box = new VBox(6);
        box.setPadding(new Insets(10, 6, 0, 6));
        box.setPrefWidth(320);
        box.getChildren().addAll(new Label("Valor"), txtValor);
        if (pedirCpf) {
            box.getChildren().addAll(new Label("CPF do destinatário"), txtCpf);
        }
        dialog.getDialogPane().setContent(box);
        Platform.runLater(txtValor::requestFocus);

        dialog.setResultConverter(b ->
                b == confirmar ? new String[]{txtValor.getText(), txtCpf.getText()} : null);
        return dialog.showAndWait();
    }

    private double lerValor(String texto) {
        try {
            String t = texto.trim().replace("R$", "").replace(" ", "");
            if (t.contains(",")) {
                t = t.replace(".", "").replace(",", ".");
            }
            double v = Double.parseDouble(t);
            return Math.round(v * 100.0) / 100.0;
        } catch (Exception e) {
            return -1;
        }
    }

    private String pedirSenha() {
        Dialog<String> dialog = new Dialog<>();
        dialog.initOwner(btnRetirar.getScene().getWindow());
        dialog.setTitle("Confirmar senha");
        dialog.setHeaderText("Digite sua senha para excluir a conta");

        ButtonType excluir = new ButtonType("Excluir", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(excluir, ButtonType.CANCEL);

        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Sua senha");
        txtSenha.setStyle(ESTILO_CAMPO);

        VBox box = new VBox(6, new Label("Senha"), txtSenha);
        box.setPadding(new Insets(10, 6, 0, 6));
        box.setPrefWidth(320);
        dialog.getDialogPane().setContent(box);
        Platform.runLater(txtSenha::requestFocus);

        dialog.setResultConverter(b -> b == excluir ? txtSenha.getText() : null);
        return dialog.showAndWait().orElse(null);
    }

    private boolean confirmar(String titulo, String texto) {
        Alert alerta = new Alert(Alert.AlertType.NONE);
        alerta.initOwner(btnRetirar.getScene().getWindow());
        alerta.setTitle(titulo);
        alerta.setHeaderText(titulo);
        alerta.setContentText(texto);
        ButtonType sim = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        ButtonType nao = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alerta.getButtonTypes().setAll(sim, nao);
        return alerta.showAndWait().orElse(nao) == sim;
    }

    private String moeda(double valor) {
        return NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(valor);
    }

    private void mostrarErro(String mensagem) {
        mostrarMsg(mensagem, ESTILO_ERRO);
    }

    private void mostrarSucesso(String mensagem) {
        mostrarMsg(mensagem, ESTILO_SUCESSO);
    }

    private void mostrarMsg(String mensagem, String estilo) {
        lblMsg.setText(mensagem);
        lblMsg.setStyle(estilo);
        lblMsg.setVisible(true);
    }

    private void esconderMsg() {
        lblMsg.setVisible(false);
    }
}