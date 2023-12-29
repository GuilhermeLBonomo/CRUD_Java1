package janela;

import javax.swing.*;

public abstract class PadraoJanela extends JFrame {

    public PadraoJanela(String titulo) {
        super(titulo);
        configurarJanela();
    }

    private void configurarJanela() {
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Adiciona suporte ao "dark mode"
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            // Outras opções: "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" para Windows
            // ou "com.apple.laf.AquaLookAndFeel" para macOS
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método abstrato que deve ser implementado nas subclasses
    protected abstract void inicializarComponentes();

    // Método para exibir a janela
    public void exibir() {
        inicializarComponentes();
        setVisible(true);
    }
}
