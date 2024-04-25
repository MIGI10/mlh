package presentation.views;

import presentation.Globals;

import presentation.views.media.player.PlayerUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * MainFrame class that represents the main view.
 *
 * @author Group 6
 * @version 1.0
 */
public class MainFrame extends JFrame {
    /**
     * JPanel representing the center panel of the view.
     */
    private final JPanel cards;
    /**
     * PlayerUI instance to be displayed.
     */
    private PlayerUI playerUI;
    /**
     * JButtons indicating the different options.
     */
    private JButton arrowButton;
    private JButton settingsButton;
    /**
     * JPanel representing the top panel of the view.
     */
    private JPanel topPanel;

    /**
     * Constructor method of AddSongUI.
     */
    public MainFrame() {
        this.cards = new JPanel(new CardLayout());
        setTitle("Espotifai");
        setAppIcon();
    }

    /**
     * Method that configures the view.
     */
    public void config() {
        configLayout();
        configWindow();
        selectScreen(Globals.MENU);
        setTopVisibility(false);
    }

    /**
     * Method that sets the icon of the app.
     */
    private void setAppIcon() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            try {
                Taskbar.getTaskbar().setIconImage(new ImageIcon("files/images/logo.png").getImage());
            } catch (UnsupportedOperationException | SecurityException ignore) {}
        }
        else {
            this.setIconImage(new ImageIcon("files/images/logo.png").getImage());
        }
    }

    /**
     * Method that configures the size of the window.
     */
    private void configWindow() {
        pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        if (toolkit.isFrameStateSupported(JFrame.MAXIMIZED_BOTH)) {
            setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        }
        else {
            Dimension size = toolkit.getScreenSize();
            setSize(size.width, size.height);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Method that configures the general layout of the view.
     */
    private void configLayout() {
        getContentPane().setLayout(new BorderLayout());
        configTop();
        getContentPane().add(cards, BorderLayout.CENTER);
        getContentPane().add(playerUI, BorderLayout.SOUTH);
        setVisible(true);
    }

    /**
     * Method that configures the top layout of the view.
     */
    private void configTop() {
        topPanel = new JPanel(new GridLayout(2, 4));
        topPanel.setBackground(Color.black);
        BufferedImage arrow;

        try {
            arrow = ImageIO.read(new File("files/images/arrow.png"));
            Image arrowImage = arrow.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            ImageIcon arrowIcon = new ImageIcon(arrowImage);
            arrowButton = new JButton(arrowIcon);
            arrowButton.setBorder(BorderFactory.createEmptyBorder());
            arrowButton.setContentAreaFilled(false);
            arrowButton.setBorderPainted(false);
            arrowButton.setFocusPainted(false);
            arrowButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            arrowButton.setActionCommand(Globals.JB_BACK_TO_MAIN);
            for (int i = 0; i < 4; i++) {
                topPanel.add(Box.createRigidArea(new Dimension(1, 1)));
            }
            topPanel.add(arrowButton);
            topPanel.add(Box.createRigidArea(new Dimension(1, 1)));
        } catch (IOException e) {
            for (int i = 0; i < 4; i++) {
                topPanel.add(Box.createRigidArea(new Dimension(1, 1)));
            }
            topPanel.add(Box.createRigidArea(new Dimension(30, 30)));
            topPanel.add(Box.createRigidArea(new Dimension(1, 1)));
        }

        //Settings icon
        BufferedImage settings;
        try {
            settings = ImageIO.read(new File("files/images/settings_icon.png"));
            Image settingsImage = settings.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            ImageIcon settingsIcon = new ImageIcon(settingsImage);
            settingsButton = new JButton(settingsIcon);
            settingsButton.setBorder(BorderFactory.createEmptyBorder());
            settingsButton.setContentAreaFilled(false);
            settingsButton.setBorderPainted(false);
            settingsButton.setFocusPainted(false);
            settingsButton.setActionCommand(Globals.JB_SETTINGS);

            topPanel.add(Box.createRigidArea(new Dimension(1, 1)));
            topPanel.add(settingsButton);
        } catch (IOException e) {
            topPanel.add(Box.createRigidArea(new Dimension(1, 1)));
            topPanel.add(Box.createRigidArea(new Dimension(30, 30)));
        }
        this.add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Method that registers a listener to the buttons.
     *
     * @param actionListener listener to be registered.
     */
    public void setMainListener(ActionListener actionListener) {
        arrowButton.addActionListener(actionListener);
        settingsButton.addActionListener(actionListener);
        arrowButton.setActionCommand(Globals.JB_BACK);
        settingsButton.setActionCommand(Globals.JB_SETTINGS);
    }

    /**
     * Method that adds a new card.
     *
     * @param panel JPanel instance to be added.
     * @param tag String indicating the label of the card.
     */
    public void addCard(JPanel panel, String tag){
        cards.add(panel, tag);
    }

    /**
     * Method that sets the player attribute.
     *
     * @param playerUI PlayerUI instance to be set.
     */
    public void setPlayer(PlayerUI playerUI) {
        this.playerUI = playerUI;
    }

    /**
     * Method that changes the current card being displayed.
     *
     * @param componentName String indicating the tag of the card to be displayed.
     */
    public void selectScreen(String componentName) {
        CardLayout cardLayout = (CardLayout) cards.getLayout();
        cardLayout.show(cards, componentName);
        showTop(componentName);
    }

    /**
     * Method that decides the visibility of the top panel.
     *
     * @param nextScreen String indicating the tag of the next screen to be displayed.
     */
    private void showTop(String nextScreen) {
        switch (nextScreen) {
            case Globals.MAIN_SCREEN -> showSettings();
            case Globals.MENU -> setTopVisibility(false);
            case Globals.SIGN_UP, Globals.LOG_IN, Globals.LOG_OUT -> showArrow();
            default -> setTopVisibility(true);
        }
    }

    /**
     * Method that changes the visibility of the player.
     *
     */
    public void togglePlayer() {
        playerUI.setVisible(!playerUI.isVisible());
    }

    /**
     * Method that shows a popup with an error.
     *
     * @param error String indicating the message to be displayed.
     */
    public void showError(String error) {
        JOptionPane.showMessageDialog(this, "ERROR: " + error, "Error Dialog", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Method that shows a popup with a message.
     *
     * @param message String indicating the message to be displayed.
     */
    public void showMessage(String message){
        JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Method that retrieves the current screen being displayed.
     *
     * @return Screen representing the card being displayed.
     */
    public Screen getCurrentCard(){
        return Objects.requireNonNull(getCurrentCardUI());
    }

    /**
     * Method that retrieves the current Screen being displayed.
     *
     * @return Screen representing the card being displayed.
     */
    private Screen getCurrentCardUI() {
        Component[] components = cards.getComponents();

        for (Component component : components) {
            if (component.isVisible()) {
                return (Screen) component;
            }
        }
        return null;
    }

    /**
     * Method that changes the visibility of the topPanel.
     *
     * @param visibility Boolean indicating the visibility of the topPanel.
     */
    public void setTopVisibility(boolean visibility){
        topPanel.setVisible(visibility);
        settingsButton.setVisible(true);
        arrowButton.setVisible(true);
    }

    /**
     * Method that makes the settings icon of the topPanel visible.
     *
     */
    public void showSettings() {
        topPanel.setVisible(true);
        settingsButton.setVisible(true);
        arrowButton.setVisible(false);

    }

    /**
     * Method that makes the arrow icon of the topPanel visible.
     *
     */
    public void showArrow() {
        topPanel.setVisible(true);
        settingsButton.setVisible(false);
        arrowButton.setVisible(true);
    }
}
