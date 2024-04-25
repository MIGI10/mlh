package presentation.controllers.media;

import business.*;
import business.entities.Song;
import business.exceptions.BusinessException;
import presentation.Globals;
import presentation.controllers.FrameController;
import presentation.views.components.JTableModel;
import presentation.views.media.PlaylistSongsUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Controller class that controls the PlaylistSongsController.
 *
 * @author Group 6
 * @version 1.0
 */
public class PlaylistSongsController extends MouseAdapter implements ActionListener, Observer, Observable, MouseListener {
    /**
     * Object of the view to be controlled.
     */
    private final PlaylistSongsUI ui;
    /**
     * Object that plays the songs.
     */
    private final Player player;
    /**
     * Object of the SongsDetailControllerUI controller
     */
    private final SongDetailsController songDetailsController;
    /**
     * Object of the song manager.
     */
    private final SongManager songManager;
    /**
     * Object of the user manager.
     */
    private final UserManager userManager;
    /**
     * Object of the main view controller.
     */
    private final FrameController frameController;
    /**
     * Object of the playlist manager.
     */
    private final PlaylistManager playlistManager;
    /**
     * Arraylist of listeners to be notified when a relevant event happens in this
     * controller's view.
     */
    private final ArrayList<Observer> listeners = new ArrayList<>();
    /**
     * Object of the first song selected whose position is to be swapped with another.
     */
    private int songToMove1;
    /**
     * Object of the second song selected whose position is to be swapped with the first selected.
     */
    private int songToMove2;

    /**
     * Constructor method of ListPlaylistsController.
     * Initializes the attributes of the object, sets this class as a listener of the view
     * and sets the view's name
     *
     * @param player object that plays the songs.
     * @param frameController main view controller.
     * @param playlistManager object of the playlist manager.
     * @param songManager object of the song manager.
     * @param songDetailsController object of the songsDetailsUI controller.
     * @param userManager object of the user manager.
     */
    public PlaylistSongsController(Player player, SongDetailsController songDetailsController, SongManager songManager,
                                   UserManager userManager, PlaylistManager playlistManager, FrameController frameController){
        this.frameController = frameController;
        this.player = player;
        this.songManager = songManager;
        this.userManager = userManager;
        this.playlistManager = playlistManager;
        this.songDetailsController = songDetailsController;
        this.songDetailsController.attach(this);
        ui = new PlaylistSongsUI();
        ui.setName(Globals.PLAYLIST_SONGS);
        ui.setListeners(this);
        frameController.addCard(ui, Globals.PLAYLIST_SONGS);
        player.attach(this);
    }

    /**
     * Method that is called to update view before entering it.
     *
     * @param playlistId id of the playlist songs to display.
     */
    public void updatePlaylist (int playlistId) {
        int songId = -1;
        String playlistName = "";
        try {
            if (playlistManager.getPlaylistWithSongs(playlistId) != null){
                playlistName = playlistManager.getPlaylistWithSongs(playlistId).getName();
            }
        }
        catch (BusinessException e){
            playlistName = "";
        }

        String[][] rawData = new String[0][];
        try {
            rawData = playlistManager.getSongsInPlaylist(playlistId);
        } catch (BusinessException e) {
            frameController.showError(e.getMessage());
        }
        String[][] data = new String[rawData.length][2];
        int[] dataIds = new int[rawData.length];

        for (int i = 0; i < rawData.length; i++){
            for (int j = 0; j < rawData.length; j++) {
                if (i == j) {
                    data[i][0] = rawData[i][0];
                    data[i][1] = rawData[i][1];
                    dataIds[i] = Integer.parseInt(rawData[i][2]);
                    break;
                }
            }
        }
        try {
            if (player.getCurrentSong() != null){
                songId = player.getCurrentSong().getId();
            }
            if (playlistManager.getPlaylistWithSongs(playlistId) != null) {
                boolean isOwner = playlistManager.getPlaylistWithSongs(playlistId).getOwner().equals(userManager.getCurrentUser());

                ui.updateTable(data, dataIds, playlistName, playlistId, songId,
                        playlistManager.getPlaylistWithSongs(playlistId).getDescription(), isOwner);
            }
        }
        catch (BusinessException e){
            frameController.showError(e.getMessage());
        }
    }

    /**
     * Method that is triggered whenever the table the view is clicked. Allows each
     * song in the playlist to be played from the table, for each song's description to be viewed
     * and for each song to be removed from the playlist.
     *
     * @param e event that caused the method to be triggered.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        JTable sourceTable = (JTable) e.getSource();

        if (frameController.getCurrentScreen().equals(Globals.AVAILABLE_SONGS) && !e.getSource().equals(ui.getTable())){
            int row = sourceTable.rowAtPoint(e.getPoint());
            int songId = Integer.parseInt((String)sourceTable.getValueAt(row, 0));
            try {
                playlistManager.addSongToPlaylist(songId, ui.getPlaylistId());
            }
            catch (BusinessException ex){
                frameController.showError(ex.getMessage());
            }
            frameController.forgetCurrentScreen();
            update();
            return;
        }

        if (frameController.getCurrentScreen().equals(Globals.SONG_DETAILS)){
            return;
        }
        int row = ui.getTable().rowAtPoint(e.getPoint());
        int col = ui.getTable().columnAtPoint(e.getPoint());
        ui.getTable().clearSelection();
        if (row < 0){
            return;
        }
        int songId = ui.getUserRowId(row);

        if (ui.getReorderingStatus() == 0) {
            if (col == 0 && row >= 0) {
                try {
                    if (player.getCurrentSong() == null || songId != player.getCurrentSong().getId()) {
                        player.queue(songManager.getSong(songId), playlistManager.getPlaylistWithSongs(ui.getPlaylistId()));
                    }
                    if (player.getCurrentSong() != null) {
                        player.togglePause();
                    }
                } catch (BusinessException ex) {
                    frameController.showError(ex.getMessage());
                }
            } else if ((col == 1 || col == 2) && row >= 0) {
                songDetailsController.setSong(songId);
                frameController.swapScreen(ui, Globals.SONG_DETAILS);
            } else if (col == 3 && row >= 0) {
                try {
                    if (userManager.getCurrentUser().equals(playlistManager.getPlaylistWithSongs(ui.getPlaylistId()).getOwner())) {
                        playlistManager.deleteSongInPlaylist(songId, ui.getPlaylistId());
                        updatePlaylist(ui.getPlaylistId());
                    }
                } catch (BusinessException ex) {
                    frameController.showError(ex.getMessage());
                }
            }
        }
        else if (ui.getReorderingStatus() == 1 && row >= 0){
            try {
                if (userManager.getCurrentUser().equals(playlistManager.getPlaylistWithSongs(ui.getPlaylistId()).getOwner())) {
                    songToMove1 = ui.getUserRowId(row);
                    ui.toggleReorder("");
                }
            } catch (BusinessException ex) {
                frameController.showError(ex.getMessage());
            }
        }
        else if (ui.getReorderingStatus() == 2 && row >= 0) {
            try {
                if (userManager.getCurrentUser().equals(playlistManager.getPlaylistWithSongs(ui.getPlaylistId()).getOwner())) {
                    songToMove2 = ui.getUserRowId(row);
                }
                songToMove2 = ui.getUserRowId(row);
                playlistManager.swapPositions(ui.getPlaylistId(), songToMove1, songToMove2);
                update();
                ui.toggleReorder("");
                if (player.getCurrentPlaylist() != null && player.getCurrentPlaylist().getId() == ui.getPlaylistId()) {
                    player.queue(playlistManager.getPlaylistWithSongs(ui.getPlaylistId()));
                }
            } catch (BusinessException ex) {
                frameController.showError(ex.getMessage());
            }
        }
    }

    /**
     * Method that is triggered whenever a change occurs in the player so that the songs
     * play buttons are synchronised with the player.
     *
     * @param message message stating what type of event happened to the player
     * @param song song that the player is currently playing.
     */
    @Override
    public void update(String message, Song song) {
        SwingUtilities.invokeLater(()->{
            JTable table = ui.getTable();
            if (song == null) {
                if (message.equals(Player.STOP)){
                    for (int i = 0; i < table.getRowCount(); i++) {
                        ui.setPlayButton(i);
                    }
                }
                return;
            }

            for (int i = 0; i < table.getRowCount(); i++) {
                ui.setPlayButton(i);

                if (table.getValueAt(i, 1).equals(song.getTitle())) {
                    if (message.equals(Player.SONG_START) || message.equals(Player.SONG_RESUME) ){
                        ui.setPauseButton(i);
                    }
                }
            }
        });
    }

    /**
     * Method that is triggered when an event in a different view happens which affects
     * this controller's view.
     *
     */
    @Override
    public void update() {
        updatePlaylist(ui.getPlaylistId());
    }

    /**
     * Method that is triggered whenever a button is clicked, this method performs, if selected,
     * the deletion of the playlist, an addition of a song to this playlist and a reordering
     * of the songs.
     *
     * @param e event that caused the method to be triggered.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("reOrder")){
            ui.toggleReorder("press");
            return;
        }
        try {
            if (userManager.getCurrentUser().equals(playlistManager.getPlaylistWithSongs(ui.getPlaylistId()).getOwner())) {
                if (e.getActionCommand().equals("addButton")) {
                    notifyObservers("adding song");
                    frameController.swapScreen(ui, Globals.AVAILABLE_SONGS);
                } else if (e.getActionCommand().equals("deleteButton")) {
                    try {
                        playlistManager.deletePlaylist(ui.getPlaylistId());
                        notifyObservers("deleted playlist");
                        frameController.forgetCurrentScreen();
                    } catch (BusinessException ex) {
                        frameController.showError(ex.getMessage());
                    }
                }
            }
        } catch (BusinessException ex) {
            frameController.showError(ex.getMessage());
        }
    }

    /**
     * Method that makes an object a listener to this object.
     *
     * @param o object that listens to this controller.
     */
    @Override
    public void attach(Observer o) {
        listeners.add(o);
    }

    /**
     * Method that removes a listener from this object.
     *
     * @param o object that listens to this controller.
     */
    @Override
    public void detach(Observer o) {
        listeners.remove(o);
    }

    /**
     * Method that notifies all listeners attached what event has happened.
     *
     * @param message that describes the event that has happened.
     */
    @Override
    public void notifyObservers(String message) {
        for (Observer listener : listeners) {
            listener.update();
        }
    }
}