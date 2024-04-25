package presentation.controllers;

import presentation.Globals;
import presentation.views.MainFrame;
import presentation.views.Screen;
import presentation.views.media.player.PlayerUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

/**
 * Controller class for the MainFrame.
 *
 * @author Group 6
 * @version 1.0
 */
public class FrameController implements ActionListener {
    /**
     * MainFrame instance to control the view being displayed.
     */
    private final MainFrame mainFrame;
    /**
     * Stack of Screens representing the previously visited screens.
     */
    private final Stack<Screen> previousScreen;
    /**
     * String representing the label of the current screen being displayed.
     */
    private String currentScreen;

    /**
     * Constructor method for AddSongController.
     *
     * @param mainFrame MainFrame instance to control the view being displayed.
     */
    public FrameController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        previousScreen = new Stack<>();
    }

    /**
     * Method that configures the mainFrame instance.
     *
     */
    public void config() {
        mainFrame.config();
        mainFrame.setMainListener(this);
    }

    /**
     * ActionListener method to process the different button clicks.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            default -> mainFrame.selectScreen(e.getActionCommand());
            case Globals.JB_BACK -> {
                mainFrame.setTopVisibility(true);
                Screen previousScreen = getPreviousScreen();
                Screen currentScreen = mainFrame.getCurrentCard();
                currentScreen.clearFields();
                mainFrame.selectScreen(previousScreen.getTag());
                previousScreen.clearFields();
            }

            case Globals.JB_SETTINGS -> {
                mainFrame.setTopVisibility(false);
                Screen currentScreen = mainFrame.getCurrentCard();
                currentScreen.clearFields();
                previousScreen.add(currentScreen);
                mainFrame.selectScreen(Globals.LOG_OUT);
            }
        }
    }

    /**
     * Method that registers a new card.
     *
     * @param panel JPanel to be registered.
     * @param tag String indicating the label to be attached to the card.
     */
    public void addCard(JPanel panel, String tag){
        mainFrame.addCard(panel, tag);
    }

    /**
     * Method that sets the player attribute of the mainFrame.
     *
     * @param playerUI PlayerUI instance to be set.
     */
    public void setPlayer(PlayerUI playerUI) {
        mainFrame.setPlayer(playerUI);
    }

    /**
     * Method that shows a popup with an error in the mainFrame.
     *
     * @param err String indicating the message to be displayed.
     */
    public void showError(String err) {
        mainFrame.showError(err);
    }

    /**
     * Method that shows a popup with a message in the mainFrame.
     *
     * @param message String indicating the message to be displayed.
     */
    public void showMessage(String message){
        mainFrame.showMessage(message);
    }

    /**
     * Method that swaps the cards of the mainFrame.
     *
     * @param currentScreen Screen instance currently being displayed.
     * @param nextScreen String indicating the tag of the next card to be displayed.
     */
    public void swapScreen(Screen currentScreen, String nextScreen){
        this.currentScreen = nextScreen;
        setPreviousScreen(currentScreen);
        mainFrame.selectScreen(nextScreen);
    }

    /**
     * Method that swaps the current card being displayed for the previous one.
     *
     */
    public void forgetCurrentScreen(){
        currentScreen = getPreviousScreenTag();
        mainFrame.selectScreen(getPreviousScreen().getTag());
    }

    /**
     * Method that adds a new screen to the previousScreen Stack
     *
     * @param screen Screen instance to be added.
     */
    public void setPreviousScreen(Screen screen){
        previousScreen.add(screen);
    }

    /**
     * Method that retrieves the previous screen that was displayed.
     *
     * @return Screen representing the previous displayed screen.
     */
    public Screen getPreviousScreen(){
        return previousScreen.pop();
    }

    /**
     * Method that retrieves the current screen being displayed.
     *
     * @return String representing the tag of the current screen.
     */
    public String getCurrentScreen(){
        return currentScreen;
    }

    /**
     * Method that retrieves the previous screen that was displayed.
     *
     * @return String representing the tag of the previous screen.
     */
    public String getPreviousScreenTag(){
        return previousScreen.get(previousScreen.size()-1).getTag();
    }

    /**
     * Method that removes everything from the previousScreen stack.
     *
     */
    public void clearPreviousScreens(){
        previousScreen.clear();
    }

    /**
     * Method that changes the visibility of the player in the mainFrame.
     *
     */
    public void togglePlayer(){
        mainFrame.togglePlayer();
    }

    /**
     * Method that returns an instance of the MainFrame.
     *
     * @return MainFrame instance.
     */
    public MainFrame getFrame() {
        return mainFrame;
    }

}