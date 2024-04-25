package business;

import business.entities.Playlist;
import business.exceptions.BusinessException;
import persistence.PlaylistDAO;
import persistence.db.DBPlaylistDAO;
import persistence.db.Database;
import persistence.exceptions.PersistenceException;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class that handles the requests of the controllers to do with playlists
 * and fetches the information of the playlistDAO to return what is needed.
 *
 * @author Group 6
 * @version 1.0
 */
public class PlaylistManager {
    /**
     * Object containing the information and methods necessary to connect to the database.
     */
    private final PlaylistDAO playlistDAO;
    /**
     * Object of the class that manages the information of the user logged in.
     */
    private final UserManager userManager;

    /**
     * Constructor method for a playlistManager object.
     * Initialises the attributes of the object.
     *
     * @param db object containing the information and methods necessary to connect to the database.
     * @param userManager object of the class that manages the information of the user logged in.
     */
    public PlaylistManager(Database db, UserManager userManager) {
        playlistDAO = new DBPlaylistDAO(db);
        this.userManager = userManager;
    }

    /**
     * Method that deletes all playlists of a username.
     *
     * @param username name of the user whose playlists are to be deleted.
     */
    public void deleteUserPlaylists(String username) throws BusinessException {
        try {
            playlistDAO.deletePlaylistsByUser(username);
        } catch (PersistenceException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Method that calls the dao to store all the attributes of this playlist in the database.
     *
     * @param playlist playlist to be created.
     * @throws BusinessException if there was an error when calling a method from the playlistDAO.
     */
    public void createPlaylist(Playlist playlist) throws BusinessException {
        try {
            playlistDAO.createPlaylist(playlist);
        } catch (PersistenceException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Method that gets the name, owner and id of each playlist in the database.
     *
     * @return matrix of strings, where each row is an array containing the name, owner and id of
     * a playlist.
     * @throws BusinessException if there was an error when calling a method from the playlistDAO.
     */
    public String[][] getAllPlaylists() throws BusinessException{

        List<Playlist> playlists;
        try {
            playlists = playlistDAO.getPlaylists();
        } catch (PersistenceException e) {
            throw new BusinessException(e);
        }

        if (playlists != null) {
            String[][] data = new String[playlists.size()][3];

            for (int i = 0; i < playlists.size(); i++) {
                data[i][0] = playlists.get(i).getName();
                data[i][1] = playlists.get(i).getOwner();
                data[i][2] = Integer.toString(playlists.get(i).getId());
            }
            return data;
        }
        return new String[0][0];
    }

    /**
     * Method that gets the names of all playlists owned by the user logged in at the moment.
     *
     * @return array of playlist names.
     * @throws BusinessException if there was an error when calling a method from the playlistDAO.
     */
    public String[] getUserPlaylistsName() throws BusinessException {

        List<Playlist> allPlaylists;
        List<String> userPlaylists = new ArrayList<>();
        try {
            allPlaylists = playlistDAO.getPlaylists();
        } catch (PersistenceException e) {
            throw new BusinessException(e);
        }

        for (int i = 0; i < allPlaylists.size(); i++) {
            if (allPlaylists.get(i).getOwner().equals(userManager.getCurrentUser())) {
                userPlaylists.add(allPlaylists.get(i).getName());
            }
        }

        return userPlaylists.toArray(new String[0]);
    }

    /**
     * Method that gets the ids of all playlists owned by the user logged in at the moment.
     *
     * @return array of playlist ids.
     * @throws BusinessException if there was an error when calling a method from the playlistDAO.
     */
    public int[] getUserPlaylistsId() throws BusinessException {

        List<Playlist> allPlaylists;
        List<Integer> userPlaylists = new ArrayList<>();
        try {
            allPlaylists = playlistDAO.getPlaylists();
        } catch (PersistenceException e) {
            throw new BusinessException(e);
        }

        for (int i = 0; i < allPlaylists.size(); i++) {
            if (allPlaylists.get(i).getOwner().equals(userManager.getCurrentUser())) {
                userPlaylists.add(allPlaylists.get(i).getId());
            }
        }

        int[] array = new int[userPlaylists.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = userPlaylists.get(i);
        }

        return array;
    }

    /**
     * Method that gets the titles, owners and ids of songs in a playlist.
     *
     * @return matrix of strings where each array contains the title, owner and id of a song.
     * @throws BusinessException if there was an error when calling a method from the playlistDAO.
     */
    public String[][] getSongsInPlaylist(int id) throws BusinessException {
        try {
            Playlist playlist = playlistDAO.getPlaylistWithSongs(id);
            String[][] songs = new String[playlist.getSongs().size()][3];

            for (int i = 0; i < playlist.getSongs().size(); i++){
                songs[i][0] = playlist.getSongs().get(i).getTitle();
                songs[i][1] = playlist.getSongs().get(i).getOwner();
                songs[i][2] = Integer.toString(playlist.getSongs().get(i).getId());
            }
            return songs;
        }
        catch (PersistenceException e){
            throw new BusinessException(e);
        }
        catch (NullPointerException ignored){
        }
        return new String[0][0];
    }

    /**
     * Method that adds a song to a playlist.
     *
     * @param songId id of the song to add to the playlist.
     * @param playlistId id of the playlist where the song will be added.
     * @return true if the song was added successfully to the playlist.
     * @throws BusinessException if there was an error when calling a method from the playlistDAO.
     */
    public boolean addSongToPlaylist(int songId, int playlistId) throws BusinessException{
        try {
            Playlist playlist = playlistDAO.getPlaylistWithSongs(playlistId);
            return playlistDAO.addSong(playlistId, songId);
        }
        catch (PersistenceException e){
            throw new BusinessException(e);
        }
    }

    /**
     * Method that gets a playlist with all of its songs.
     *
     * @param id id of the playlist to get.
     * @return playlist with all of its songs inside.
     * @throws BusinessException if there was an error when calling a method from the playlistDAO.
     */
    public Playlist getPlaylistWithSongs(int id) throws BusinessException{
        try {
            return playlistDAO.getPlaylistWithSongs(id);
        }
        catch (PersistenceException e){
            throw new BusinessException(e);
        }
    }

    /**
     * Method that calls the playlist dao to delete a playlist from the database
     *
     * @param  id id of the playlist to be deleted.
     * @throws BusinessException if there was an error when calling a method from the playlistDAO.
     */
    public void deletePlaylist(int id) throws BusinessException{
        try {
            playlistDAO.deletePlaylist(id);
        }
        catch (PersistenceException e){
            throw new BusinessException(e);
        }
    }

    /**
     * Method that deletes a song from a playlist.
     *
     * @param songId id of the song to be deleted from the playlist.
     * @param playlistId id of the playlist where the song will be deleted from.
     * @throws BusinessException if there was an error when calling a method from the playlistDAO.
     */
    public void deleteSongInPlaylist(int songId, int playlistId) throws BusinessException{
        try {
            Playlist playlist = playlistDAO.getPlaylistWithSongs(playlistId);
            for (int i = 0; i < playlist.getSongs().size(); i++){
                if (playlist.getSongs().get(i).getId() == songId){
                    playlistDAO.removeSong(playlistId, songId);
                }
            }
        } catch (PersistenceException e){
            throw new BusinessException(e);
        }
    }

    /**
     * Method that swaps the position of two songs in a playlist.
     *
     * @param playlistId id of the playlist where the order of the songs will be changed.
     * @param songId1 id of one of the songs whose positions are to be swapped
     * @param songId2 id of the other song whose position  will be swapped
     * @throws BusinessException if there was an error when calling a method from the playlistDAO.
     */
    public void swapPositions(int playlistId, int songId1, int songId2) throws BusinessException {
        try {
            int position1 = 0;
            int position2 = 0;
            Playlist playlist = playlistDAO.getPlaylistWithSongs(playlistId);
            for (int i = 0; i < playlist.getSongs().size(); i++) {
                if (songId1 == playlist.getSongs().get(i).getId()) {
                    position1 = playlist.getSongs().get(i).getPosition();
                }
                if (songId2 == playlist.getSongs().get(i).getId()) {
                    position2 = playlist.getSongs().get(i).getPosition();
                }
            }
            playlistDAO.setPosition(playlistId, songId2, position1);
            playlistDAO.setPosition(playlistId, songId1, position2);
        }
        catch (PersistenceException e){
            throw new BusinessException(e);
        }
    }
}
