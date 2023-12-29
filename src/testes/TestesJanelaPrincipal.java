package testes;

public final class TestesJanelaPrincipal extends janela.PadraoJanela {

    public TestesJanelaPrincipal() {
        super("Minha Janela Swing");
    }

    @Override
    protected void inicializarComponentes() {
        javax.swing.JButton botao = new javax.swing.JButton("Clique-me!");
        getContentPane().add(botao);
        // Adicione mais componentes conforme necessário
    }

    public static void main(String[] args) {
        TestesJanelaPrincipal minhaJanela = new TestesJanelaPrincipal();
        minhaJanela.exibir();
    }
}

