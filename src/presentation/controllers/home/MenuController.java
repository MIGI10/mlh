package presentation.controllers.home;

import presentation.Globals;
import presentation.controllers.FrameController;
import presentation.views.home.MenuUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Controller class that controls the menuUI
 *
 * @author Group 6
 * @version 1.0
 */
public class MenuController extends MouseAdapter implements ActionListener {
    /**
     * Object of the view to be controlled.
     */
    private final MenuUI ui;
    /**
     * Controller of the main view.
     */
    private final FrameController frameController;

    /**
     * Constructor method of MenuController.
     * Initializes the attributes of the object, sets this class as a listener of the view
     * and sets the view's name
     *
     * @param frameController main view controller.
     */
    public MenuController(FrameController frameController){
        this.ui = new MenuUI();
        this.frameController = frameController;
        frameController.addCard(ui, Globals.MENU);
        ui.setListeners(this);
        ui.setName(Globals.MENU);
    }

    /**
     * Method that is triggered whenever a component with this object as a listener has an action
     * performed on them.
     *
     * @param e event that caused the method to be triggered.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        frameController.swapScreen(ui, e.getActionCommand());
    }

    /**
     * Method that is triggered whenever the cursor is entered in a component with
     * this object as a listener to them.
     *
     * @param e event that caused the method to be triggered.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        ui.setButtonForeground((JButton) e.getSource(), Color.white);
    }

    /**
     * Method that is triggered whenever the cursor exits a component with
     * this object as a listener to them.
     *
     * @param e event that caused the method to be triggered.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        ui.setButtonForeground((JButton) e.getSource(), Color.black);
    }

}
