package presentation.views.media;

import presentation.Globals;
import presentation.views.Screen;
import presentation.views.components.JTableModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * UI class that is displays a playlist.
 *
 * @author Group 6
 * @version 1.0
 */
public class PlaylistSongsUI extends Screen {
    /**
     * JTableModel of the table that displays the songs in a playlist.
     */
    private final JTableModel tableModel;
    /**
     * JLabel with the name of the playlist.
     */
    private final JLabel playlistName;
    /**
     * JLabel with the playlist description.
     */
    private final JLabel playlistDesc;
    /**
     * JButton that needs to be pressed in order to play that song.
     */
    private final JButton playButton;
    /**
     * JButton to be pressed for the song to pause.
     */
    private final JButton pauseButton;
    /**
     * JButton to be pressed for the song in that row to be deleted from the playlist.
     */
    private final JButton binButton;
    /**
     * JButton to be pressed to add a song to the playlist.
     */
    private final JButton addButton;
    /**
     * JButton to be pressed to delete the playlist.
     */
    private final JButton deleteButton;
    /**
     * JButton to be pressed in order to start reordering songs or to exit reordering state.
     */
    private final JButton reorderButton;
    /**
     * Jid of the playlist that is being displayed.
     */
    private int playlistId;
    /**
     * int which olds the reordering state the playlist is in.
     */
    private int reordering;

    /**
     * Constructor method that initialises all the components, their positions and aesthetics
     * which will be showed when selecting this view.
     *
     */
    public PlaylistSongsUI(){
        reordering = 0;

        setLayout(new BorderLayout());
        setBackground(Color.black);

        playButton = loadButton("files/images/play_button_table.png", 15, 15);
        pauseButton = loadButton("files/images/pause_button_table.png", 15, 15);
        binButton = loadButton("files/images/bin_table.png", 15, 15);
        deleteButton = loadButton("files/images/bin_table.png", 30, 30);
        addButton = loadButton("files/images/add_song_table.png", 30, 30);

        reorderButton = new JButton("REORDER");
        reorderButton.setFont(new Font("SIGN UP", Font.BOLD, 15));
        reorderButton.setMaximumSize(new Dimension(100, 30));
        reorderButton.setMinimumSize(new Dimension(100, 30));
        reorderButton.setBorderPainted(false);
        reorderButton.setFocusPainted(false);
        reorderButton.setBackground(Globals.greenSpotify);
        reorderButton.setForeground(Color.black);
        reorderButton.setOpaque(true);

        JPanel topGrid = new JPanel(new GridLayout(2, 1));
        topGrid.setBackground(Color.black);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.BLACK);
        playlistName = new JLabel("AVAILABLE PLAYLISTS");
        playlistName.setFont(new Font("Calibri", Font.BOLD, 30));
        playlistName.setForeground(Globals.greenSpotify);
        titlePanel.add(playlistName);
        titlePanel.add(Box.createRigidArea(new Dimension(150, 1)));
        titlePanel.add(addButton);
        titlePanel.add(Box.createRigidArea(new Dimension(10, 1)));
        titlePanel.add(deleteButton);
        titlePanel.add(Box.createRigidArea(new Dimension(10, 1)));
        titlePanel.add(reorderButton);
        topGrid.add(titlePanel);

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.PAGE_AXIS));
        descriptionPanel.setBackground(Color.black);
        descriptionPanel.add(Box.createRigidArea(new Dimension(1, 15)));

        playlistDesc = new JLabel("There is currently no description available for this playlist");
        playlistDesc.setFont(new Font("Calibri", Font.ITALIC, 20));
        playlistDesc.setForeground(Globals.greenSpotify);
        playlistDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        descriptionPanel.add(playlistDesc);
        descriptionPanel.add(Box.createRigidArea(new Dimension(1, 15)));

        topGrid.add(descriptionPanel);
        this.add(topGrid, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new FlowLayout());
        tablePanel.setBackground(Color.black);

        tableModel = new JTableModel(new String[]{" ", "SONGS", "OWNER", " "});
        tablePanel.add(tableModel.getScrollPane(), BorderLayout.WEST);
        this.add(tablePanel, BorderLayout.CENTER);

    }

    /**
     * Method that loads a button.
     *
     * @param path of the png, which is what the button will look like.
     * @param width of the button.
     * @param length of the button.
     * @return new JButton loaded from a png.
     */
    private JButton loadButton(String path, int width, int length){
        JButton jButton;
        try {
            BufferedImage buttonBuffered;
            buttonBuffered = ImageIO.read(new File(path));
            Image buttonImage = buttonBuffered.getScaledInstance(width, length, Image.SCALE_DEFAULT);
            ImageIcon buttonIcon = new ImageIcon(buttonImage);
            jButton = new JButton(buttonIcon);
            jButton.setBorder(BorderFactory.createEmptyBorder());
            jButton.setContentAreaFilled(false);
            jButton.setBorderPainted(false);
            jButton.setFocusPainted(false);
        } catch (IOException e) {
            jButton = new JButton();
        }
        return jButton;
    }

    /**
     * Method that updates the playlist songs table.
     *
     * @param data to fill the table with.
     * @param dataIds ids of the song in each row.
     * @param playlistName name of the playlist.
     * @param playlistId id of the playlist.
     * @param songPlayed song that is playing at the moment of updating the table.
     * @param description description of the playlist.
     * @param isOwner indicates if the currently logged-in user owns the playlist or not.
     */
    public void updateTable(String[][] data, int[] dataIds, String playlistName, int playlistId, int songPlayed, String description, boolean isOwner) {
        this.playlistName.setText(playlistName);
        this.playlistId = playlistId;
        if (description.length() > 0)
            this.playlistDesc.setText(description);

        Object[][] finalData;
        finalData = new Object[data.length][4];

        for (int i = 0; i < data.length; i++){
            if (songPlayed == dataIds[i]) {
                finalData[i][0] = pauseButton;
            }
            else {
                finalData[i][0] = playButton;
            }
            finalData[i][1] = data[i][0];
            finalData[i][2] = data[i][1];
            finalData[i][3] = binButton;
        }
        tableModel.updateTable(finalData, dataIds);
        tableModel.setColumnWidth(0, 60);
        tableModel.setColumnWidth(3, 60);

        addButton.setVisible(isOwner);
        deleteButton.setVisible(isOwner);
        reorderButton.setVisible(isOwner);
    }

    /**
     * Method that adds the listener specified to the components necessary.
     *
     * @param listener that will listen to the components.
     */
    public void setListeners(MouseListener listener){
        tableModel.getTable().addMouseListener(listener);
        addButton.addActionListener((ActionListener) listener);
        deleteButton.addActionListener((ActionListener) listener);
        reorderButton.addActionListener((ActionListener) listener);

        reorderButton.setActionCommand("reOrder");
        addButton.setActionCommand("addButton");
        deleteButton.setActionCommand("deleteButton");
    }

    /**
     * Makes the song's button show as a play button.
     *
     * @param row indicating where the song is in the table.
     */
    public void setPlayButton(int row){
        tableModel.changeCellObject(row, 0, playButton);
    }

    /**
     * Makes the song's button show as a pause button.
     *
     * @param row indicating where the song is in the table.
     */
    public void setPauseButton(int row){
        tableModel.changeCellObject(row, 0, pauseButton);
    }

    /**
     * Getter method that returns the playlist id.
     *
     * @return the playlist id.
     */
    public int getPlaylistId(){
        return playlistId;
    }

    /**
     * Getter method that returns the table with all the playlist songs.
     *
     * @return the table with all the playlist songs.
     */
    public JTable getTable(){
        return tableModel.getTable();
    }

    /**
     * Returns the id of the song in the playlist songs table.
     *
     * @param row indicating where the song is in the table.
     */
    public int getUserRowId(int row){
        return tableModel.getRowId(row);
    }

    /**
     * Increments the reordering state by one unless it is 2, then it resets to 0.
     *
     * @param message indicates if the state needs to change due to a button press or due to a song press.
     */
    public void toggleReorder(String message){
        if (reordering == 2) {
            reordering = 0;
            reorderButton.setBackground(Globals.greenSpotify);
        }
        else if (reordering == 1){
            if (message.equals("press")){
                reordering = 0;
                reorderButton.setBackground(Globals.greenSpotify);
            }
            else {
                reordering = 2;
            }
        }
        else {
            reordering = 1;
            reorderButton.setBackground(new Color(85, 85, 85));
        }
    }

    /**
     * Getter for the reordering status.
     *
     * @return reordering status.
     */
    public int getReorderingStatus(){
        return reordering;
    }

    /**
     * Implementation of an abstract method which unselects the reordering button if the view is changed.
     *
     */
    @Override
    public void clearFields() {
        reorderButton.setBackground(Globals.greenSpotify);
    }

    /**
     * Implementation of the abstract method which gets the tag associated to the UI.
     *
     * @return the tag associated to the UI.
     */
    @Override
    public String getTag() {
        return getName();
    }
}
