import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class CoinFlipper extends JFrame {
    private JLabel resultLabel;
    private JButton flipButton;
    private Timer flipAnimationTimer;
    private String[] outcomes = {"Heads", "Tails"};
    private Random random = new Random();

    public CoinFlipper() {
        // Start of UI
        setTitle("Coin Flipper");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Make a circle around heads tails
        JPanel resultPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw a circle around the word
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(10, 10, getWidth() - 20, getHeight() - 20);
            }
        };
        resultPanel.setPreferredSize(new Dimension(200, 200));
        resultPanel.setLayout(new GridBagLayout()); // Centre it

        // display the result
        resultLabel = new JLabel("Flip the coin!", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        resultPanel.add(resultLabel);

        // Button to flip the coin
        flipButton = new JButton("Flip");
        flipButton.setFont(new Font("Arial", Font.PLAIN, 18));
        flipButton.setPreferredSize(new Dimension(100, 100));
        flipButton.setFocusPainted(false);
        flipButton.setContentAreaFilled(false);
        flipButton.setBorderPainted(false);
        flipButton.setOpaque(false);

        // Make the button circular no matter the window size
        flipButton.setUI(new CoinButtonUI());
        flipButton.addActionListener(new FlipAction());

        // Add shit to make it centred
        setLayout(new BorderLayout());
        add(resultPanel, BorderLayout.CENTER);
        add(flipButton, BorderLayout.SOUTH);
    }

    private class FlipAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            flipButton.setEnabled(false); // Disable button while animating

            // Make it do a lil animation to show that its flipping
            flipAnimationTimer = new Timer(100, new ActionListener() {
                int count = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (count < 10) { // Animate 10 times
                        // Randomize the text between "Heads" and "Tails"
                        resultLabel.setText(outcomes[random.nextInt(outcomes.length)]);
                        count++;
                    } else {
                        // Show the final result
                        resultLabel.setText(outcomes[random.nextInt(outcomes.length)]);
                        flipAnimationTimer.stop(); // Stop the animation
                        flipButton.setEnabled(true); // Re-enable button
                    }
                }
            });

            flipAnimationTimer.start(); // Start the animation
        }
    }

    // Custom UI for making the button  circular
    private static class CoinButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            JButton button = (JButton) c;
            Graphics2D g2d = (Graphics2D) g.create();

            // Anti-aliasing for smoother button
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw button
            int diameter = Math.min(button.getWidth(), button.getHeight());
            g2d.setColor(button.getModel().isPressed() ? Color.LIGHT_GRAY : Color.YELLOW);
            g2d.fillOval(0, 0, diameter, diameter);

            // Draw text
            g2d.setColor(Color.BLACK);
            g2d.setFont(button.getFont());
            FontMetrics fm = g2d.getFontMetrics();
            int stringWidth = fm.stringWidth(button.getText());
            int stringHeight = fm.getAscent();
            g2d.drawString(button.getText(), (diameter - stringWidth) / 2, (diameter + stringHeight) / 2 - 4);

            g2d.dispose();
        }

        @Override
        public Dimension getPreferredSize(JComponent c) {
            return new Dimension(100, 100); 
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
    }

    public static void main(String[] args) {
        // Create and display the window
        SwingUtilities.invokeLater(() -> {
            CoinFlipper coinFlipper = new CoinFlipper();
            coinFlipper.setVisible(true);
        });
    }
}
