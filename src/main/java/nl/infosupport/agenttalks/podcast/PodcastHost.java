package nl.infosupport.agenttalks.podcast;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "podcast_host")
public class PodcastHost extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name", columnDefinition = "varchar(250)", nullable = false)
    public String name;

    @Column(name = "style_instructions", columnDefinition = "text", nullable = true)
    public String styleInstructions;

    @Column(name = "language_patterns", columnDefinition = "text", nullable = false)
    public String languagePatterns;

    @Column(name = "voice_id", columnDefinition = "varchar(250)", nullable = false)
    public String voiceId;

    @Column(name = "host_index", columnDefinition = "int", nullable = false)
    public int index;

    public PodcastHost() {
        // Default constructor for JPA
    }

    public PodcastHost(String name, String styleInstructions, String languagePatterns, String voiceId, int index) {
        this.name = name;
        this.styleInstructions = styleInstructions;
        this.languagePatterns = languagePatterns;
        this.voiceId = voiceId;
        this.index = index;
    }

    public static PodcastHost findByIndex(int index) {
        return find("podcast_host.index = :index", Parameters.with("index", index)).firstResult();
    }

    public static PodcastHost findByName(String name) {
        return find("name", name).firstResult();
    }

    public static PodcastHost findFirstHost() {
        return find("index = 1").firstResult();
    }

    public static PodcastHost findSecondHost() {
        return find("index = 2").firstResult();
    }
}
