import javax.swing.*;

public class Frame extends JFrame {
    // constructor
    Frame() {
        // frame-config
        this.add(new Panel());
        this.setTitle("PONG");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
