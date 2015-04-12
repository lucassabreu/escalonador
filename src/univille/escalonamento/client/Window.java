package univille.escalonamento.client;

import javax.swing.UIManager;

import univille.escalonamento.client.window.WindowImpl;

public class Window {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Nao foi possivel trocar o LookAndFeel");
        }

        WindowImpl win = new WindowImpl();
        win.setVisible(true);
    }
}
