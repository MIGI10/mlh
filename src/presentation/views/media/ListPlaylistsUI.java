package presentation.views.media;

import presentation.Globals;
import presentation.views.Screen;
import presentation.views.components.JTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * UI class that is displays the user playlists and the system playlists in two tables.
 *
 * @author Group 6
 * @version 1.0
 */
public class ListPlaylistsUI extends Screen {
    /**
     * JTableModel of the table that displays all playlists in the system.
     */
    private JTableModel globalModel;
    /**
     * JTableModel of the table that displays all playlists the user logged in owns.
     */
    private JTableModel userModel;

    /**
     * Constructor method that initialises all the components, their positions and aesthetics
     * which will be showed when selecting this view.
     *
     */
    public ListPlaylistsUI(){

        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);
        this.add(Box.createRigidArea(new Dimension(1,40)), BorderLayout.SOUTH);

        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new BorderLayout());
        tablesPanel.setBackground(Color.BLACK);

        JPanel flowLayoutTitle = new JPanel(new FlowLayout());
        flowLayoutTitle.setBackground(Color.BLACK);
        JLabel title = new JLabel("AVAILABLE PLAYLISTS");
        title.setFont(new Font("Calibri", Font.BOLD, 30));
        title.setForeground(Globals.greenSpotify);
        flowLayoutTitle.add(title);
        flowLayoutTitle.add(Box.createRigidArea(new Dimension(675, 10)));
        tablesPanel.add(flowLayoutTitle, BorderLayout.NORTH);

        JPanel flowLayoutTables = new JPanel(new FlowLayout());
        flowLayoutTables.setBackground(Color.BLACK);
        configTables(flowLayoutTables);
        tablesPanel.add(flowLayoutTables, BorderLayout.CENTER);

        this.add(tablesPanel, BorderLayout.CENTER);
    }

    /**
     * Method that configures the tables in this view.
     *
     * @param tablesPanel panel which will contain the tables.
     */
    private void configTables(JPanel tablesPanel){
        globalModel = new JTableModel(new String[]{"GLOBAL LIBRARY", "OWNER"});
        userModel = new JTableModel(new String[]{"MY LIBRARY", "OWNER"});

        tablesPanel.add(globalModel.getScrollPane());
        tablesPanel.add(Box.createRigidArea(new Dimension(40, 40)));
        tablesPanel.add(userModel.getScrollPane());
    }

    /**
     * Method that sets the listeners to the tables
     *
     * @param listener that will be listening to the events of the tables.
     */
    public void setListeners(MouseListener listener){
        globalModel.getTable().addMouseListener(listener);
        userModel.getTable().addMouseListener(listener);
    }

    /**
     * Method that updates both tables.
     *
     * @param allPlaylistsRaw information about all the playlists in a matrix of strings form.
     * @param currentUser name of the user that is currently logged in.
     */
    public void updatePlaylists(String[][] allPlaylistsRaw, String currentUser){

        String[][] allPlaylists = new String[allPlaylistsRaw.length][2];
        int[] allPlaylistsIds = new int[allPlaylistsRaw.length];

        int userPlaylistsIndex = 0;
        for (String[] strings : allPlaylistsRaw) {
            if (strings[1].equalsIgnoreCase(currentUser)) {
                userPlaylistsIndex++;
            }
        }
        String[][] userPlaylists = new String[userPlaylistsIndex][2];
        int[] userPlaylistsIds = new int[userPlaylistsIndex];
        userPlaylistsIndex = 0;

        for (int i = 0; i < allPlaylistsRaw.length; i++) {
            allPlaylists[i][0] = allPlaylistsRaw[i][0];
            allPlaylists[i][1] = allPlaylistsRaw[i][1];
            allPlaylistsIds[i] = Integer.parseInt(allPlaylistsRaw[i][2]);

            if (allPlaylistsRaw[i][1].equalsIgnoreCase(currentUser)) {
                userPlaylists[userPlaylistsIndex][0] = allPlaylistsRaw[i][0];
                userPlaylists[userPlaylistsIndex][1] = allPlaylistsRaw[i][1];
                userPlaylistsIds[userPlaylistsIndex] = Integer.parseInt(allPlaylistsRaw[i][2]);
                userPlaylistsIndex++;
            }
        }
        globalModel.updateTable(allPlaylists, allPlaylistsIds);
        userModel.updateTable(userPlaylists, userPlaylistsIds);
    }

    /**
     * Getter that returns the table with all playlists in the system.
     *
     * @return the table with all playlists in the system.
     */
    public JTable getGlobalTable(){
        return globalModel.getTable();
    }

    /**
     * Getter that returns the table with all playlists owned by the user currently logged in.
     *
     * @return the table with all playlists owned by the user currently logged in.
     */
    public JTable getUserTable(){
        return userModel.getTable();
    }

    /**
     * Gets the id of the playlist in the row selected of the system's playlists table.
     *
     * @return the id of the playlist in the row selected.
     */
    public int getGlobalRowId(int row){
        return globalModel.getRowId(row);
    }

    /**
     * Gets the id of the playlist in the row selected of the user's playlists table.
     *
     * @return the id of the playlist in the row selected of the user's playlists table.
     */
    public int getUserRowId(int row){
        return userModel.getRowId(row);
    }

    @Override
    public void clearFields() {}

    /**
     * Implementation of the abstract method which gets the tag associated to the UI.
     *
     * @return the tag associated to the UI.
     */
    @Override
    public String getTag() {
        return getName();
    }

    /**
     * Method that returns the id of the playlist selected in the system playlists table.
     *
     * @param point point where the playlist is positioned in the table.
     * @return the id of the playlist selected.
     */
    public int getPlayListAtPointGlobalTable(Point point) {
        int playlistInfo = 0;
        int row;
        int col;

        row = getGlobalTable().rowAtPoint(point);
        col = getGlobalTable().columnAtPoint(point);
        getGlobalTable().clearSelection();

        if ((col == 0 || col == 1) && row >= 0) {
            playlistInfo = getGlobalRowId(row);
        }

        return playlistInfo;
    }

    /**
     * Method that returns the id of the playlist selected in the user's playlists table.
     *
     * @param point point where the playlist is positioned in the table.
     * @return the id of the playlist selected.
     */
    public int getPlayListAtPointUserTable(Point point) {
        int playlistInfo = 0;
        int row;
        int col;
        row = getUserTable().rowAtPoint(point);
        col = getUserTable().columnAtPoint(point);
        getUserTable().clearSelection();

        if ((col == 0 || col == 1) && row >= 0) {
            playlistInfo = getUserRowId(row);
        }

        return playlistInfo;
    }
}
