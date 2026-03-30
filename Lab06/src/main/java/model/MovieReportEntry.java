package model;

// Flat model that maps directly to one row of the movies_report database view.
// Used only for generating the HTML report — avoids joining objects manually.
public class MovieReportEntry {
    private final String title;
    private final String releaseDate;
    private final int duration;
    private final double score;
    private final String genre;
    private final String actors;  // comma-separated, as returned by the view

    public MovieReportEntry(String title, String releaseDate, int duration,
                            double score, String genre, String actors) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.score = score;
        this.genre = genre;
        this.actors = actors != null ? actors : "—";
    }

    public String getTitle()       { return title; }
    public String getReleaseDate() { return releaseDate; }
    public int getDuration()       { return duration; }
    public double getScore()       { return score; }
    public String getGenre()       { return genre != null ? genre : "—"; }
    public String getActors()      { return actors; }
}