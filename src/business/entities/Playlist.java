package business.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a playlist.
 *
 * @author Group 6
 * @version 1.0
 */
public class Playlist {
    /**
     * id of the playlist.
     */
    private int id;
    /**
     * Name of the playlist.
     */
    private final String name;
    /**
     * Name of the owner of the playlist.
     */
    private final String owner;
    /**
     * Description of the playlist.
     */
    private final String description;
    /**
     * Songs contained in the playlist.
     */
    private final List<Song> songs;

    /**
     * Constructor of Playlist.
     * Creates an object of a playlist with the attributes passed as parameters.
     *
     * @param id id of the playlist.
     * @param name name of the playlist.
     * @param owner name of the owner of the playlist.
     * @param description description of the playlist.
     */
    public Playlist(int id, String name, String owner, String description) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        songs = new ArrayList<>();
    }

    /**
     * Constructor of Playlist.
     * Creates an object of a playlist with the attributes passed as parameters.
     *
     * @param name name of the playlist.
     * @param owner name of the owner of the playlist.
     * @param description description of the playlist.
     */
    public Playlist(String name, String owner, String description) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        songs = new ArrayList<>();
    }

    /**
     * Method that returns the id of the playlist.
     *
     * @return id of the playlist.
     */
    public int getId() {
        return id;
    }

    /**
     * Method that returns the name of the playlist.
     *
     * @return name of the playlist.
     */
    public String getName() {
        return name;
    }

    /**
     * Method that returns the name of the user who owns the playlist.
     *
     * @return time in MM:SS format.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Method that returns the name of the playlist.
     *
     * @return description of the playlist.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method that returns a list of the songs the playlist contains.
     *
     * @return list of songs in the playlist.
     */
    public List<Song> getSongs() {
        return List.copyOf(songs);
    }

    /**
     * Method that adds a song to the playlist.
     *
     * @param song song to add to the playlist.
     */
    public void addSong(Song song) {
        songs.add(song);
    }
}
