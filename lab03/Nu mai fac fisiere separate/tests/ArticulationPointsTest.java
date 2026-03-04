import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import People.*;
import java.util.List;

public class ArticulationPointsTest {
    private SocialNetwork network;

    @Before
    public void setUp() {
        network = new SocialNetwork();
    }

    @Test
    public void testBridgeNodeDiscovery() {
        network.addProfile(new Company("CenterNode"));
        network.addProfile(new Person("P1"));
        network.addProfile(new Person("P2"));
        network.addProfile(new Person("P3"));
        network.addProfile(new Person("P4"));

        network.addRelationship(1, 2);
        network.addRelationship(2, 3);
        network.addRelationship(3, 1);
        network.addRelationship(0, 1);
        network.addRelationship(0, 4);

        ArticulationPoints ap = new ArticulationPoints(network.getProfiles());
        List<Profile> cuts = ap.find();

        assertNotNull(cuts);
        assertFalse(cuts.isEmpty());

        boolean foundCenter = false;
        for (Profile p : cuts) {
            if (p.getName().equals("CenterNode")) {
                foundCenter = true;
                break;
            }
        }
        assertTrue(foundCenter);
    }

    @Test
    // In timp ce rulam testele am aflat ca logica mea de gestionare la ID era gresit
    // Pentru ca nu resetam niciodata ID-ul. Adica daca 2 networkuri exista in acelasi timp, o sa imparta acelasi maxID.
    // 10/10 laboratorul asta a fost mai bun decat celalalt
    // Acum ma duc sa ma mai interesez sa vad de ce sunt cu adevarat utile Genericurile astea si de ce polimorfismul static nu e suficient
    public void testSingleComponentNoCut() {
        network.addProfile(new Person("A"));
        network.addProfile(new Person("B"));
        network.addProfile(new Person("C"));

        network.addRelationship(0, 1);
        network.addRelationship(1, 2);
        network.addRelationship(0, 2);

        ArticulationPoints ap = new ArticulationPoints(network.getProfiles());
        List<Profile> cuts = ap.find();

        assertTrue(cuts.isEmpty());
    }
}