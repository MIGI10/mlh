package presentation.controllers.media;

import business.Observer;
import business.PlaylistManager;
import business.UserManager;
import business.entities.Song;
import business.exceptions.BusinessException;
import presentation.Globals;
import presentation.controllers.FrameController;
import presentation.views.media.ListPlaylistsUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Controller class that controls the ListPlaylistsUI
 *
 * @author Group 6
 * @version 1.0
 */
public class ListPlaylistsController extends MouseAdapter implements Observer {
    /**
     * Object of the view to be controlled.
     */
    private final ListPlaylistsUI ui;
    /**
     * Object of the playlist manager.
     */
    private final PlaylistManager playlistManager;
    /**
     * Object of the PlaylistSongsUI controller.
     */
    private final PlaylistSongsController playlistSongsController;
    /**
     * Object of the user controller.
     */
    private final UserManager userManager;
    /**
     * Object of the main view controller.
     */
    private final FrameController frameController;

    /**
     * Constructor method of ListPlaylistsController.
     * Initializes the attributes of the object, sets this class as a listener of the view
     * and sets the view's name
     *
     * @param frameController main view controller.
     * @param playlistManager object of the playlist manager.
     * @param playlistSongsController object of the PlaylistSongsUI controller.
     * @param userManager object of the user manager.
     */
    public ListPlaylistsController(FrameController frameController, PlaylistManager playlistManager,
                                   PlaylistSongsController playlistSongsController, UserManager userManager){
        this.playlistManager = playlistManager;
        this.playlistSongsController = playlistSongsController;
        this.playlistSongsController.attach(this);
        this.frameController = frameController;
        this.userManager = userManager;
        ui = new ListPlaylistsUI();
        ui.setName(Globals.AVAILABLE_PLAYLISTS);
        ui.setListeners(this);
        frameController.addCard(ui, Globals.AVAILABLE_PLAYLISTS);
    }

    /**
     * Method that passes all the information necessary for the playlists tables to update.
     *
     */
    public void updatePlaylistsData(){
        try {
            String[][] allPlaylistsRaw = playlistManager.getAllPlaylists();
            ui.updatePlaylists(allPlaylistsRaw, userManager.getCurrentUser());
        }
        catch (BusinessException e){
            frameController.showError(e.getMessage());
        }
    }

    /**
     * Method that is triggered whenever a component with this object as a listener
     * is clicked.
     *
     * @param e event that caused the method to be triggered.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        int playlistId;

        if (e.getSource() == ui.getGlobalTable()) {
            playlistId = ui.getPlayListAtPointGlobalTable(e.getPoint());
            playlistSongsController.updatePlaylist(playlistId);
            frameController.swapScreen(ui, Globals.PLAYLIST_SONGS);
        }
        else if (e.getSource() == ui.getUserTable()){
            playlistId = ui.getPlayListAtPointUserTable(e.getPoint());
            playlistSongsController.updatePlaylist(playlistId);
            frameController.swapScreen(ui, Globals.PLAYLIST_SONGS);
        }
    }

    @Override
    public void update(String message, Song song) {}

    /**
     * Implementation of abstract method used to update a view before transitioning to it.
     *
     */
    @Override
    public void update() {
        updatePlaylistsData();
    }

}
