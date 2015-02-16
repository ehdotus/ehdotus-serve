package ehdotus;

import au.com.bytecode.opencsv.CSVReader;
import ehdotus.domain.DifficultyData;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataServer {

    @Value("${data.file}")
    private Resource resource;
    private RestTemplate restTemplate;
    private Map<String, Integer> biggestAssignmentSubmitted;

    public DataServer() {
        this.restTemplate = new RestTemplate();
        this.biggestAssignmentSubmitted = new TreeMap<>();
    }

    public void run() {
        while (true) {
            waitAMoment();
            DifficultyData d = new DifficultyData();

            try {
                String[] entry = getEntry();
                d.setCourse("NA");
                d.setUserId(entry[0]);
                assignAssignmentData(d, entry);

                if (d.getContent().get("WORKED_ON").isEmpty()) {
                    continue;
                }

                restTemplate.postForObject("http://ehdotus.herokuapp.com/difficulty", d, String.class);

            } catch (Throwable t) {
                t.printStackTrace();
                continue;
            }

        }
    }

    private void waitAMoment() {
        try {
            Thread.sleep(1000 + new Random().nextInt(25000));
        } catch (InterruptedException ex) {
            Logger.getLogger(DataServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String[] getEntry() throws FileNotFoundException, IOException {
        // TODO: get up-to-date content

        CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(resource.getInputStream())), ';');
        List<String[]> content = reader.readAll();
        return content.get(1 + new Random().nextInt(content.size() - 1));
    }

    private void assignAssignmentData(DifficultyData data, String[] content) {
        String userId = data.getUserId();
        if (userId == null || userId.isEmpty()) {
            return;
        }

        if (!this.biggestAssignmentSubmitted.containsKey(userId)) {
            this.biggestAssignmentSubmitted.put(userId, 1);
        }

        int assignmentId = this.biggestAssignmentSubmitted.get(userId);
        if (assignmentId > 108) {
            return;
        }

        this.biggestAssignmentSubmitted.put(userId, this.biggestAssignmentSubmitted.get(userId) + 1);

        int startingIndex = 4 + (assignmentId - 1) * 11;
        data.setExercise("" + assignmentId);
        data.addEntry("WORKED_ON", content[startingIndex]);
        data.addEntry("SUBMITTED", content[startingIndex + 1]);
        data.addEntry("SECONDS_SPENT_ON", content[startingIndex + 2]);
        data.addEntry("PERCENTAGE_COMPILES", content[startingIndex + 3]);

        try {
            data.setRealDifficulty(Integer.parseInt(content[startingIndex + 4]));
        } catch (Throwable t) {
        }

        data.addEntry("NUM_OF_STATES", content[startingIndex + 6]);
        data.addEntry("SECONDS_IN_COMPILING_STATE", content[startingIndex + 7]);
        data.addEntry("SECONDS_IN_NON_COMPILING_STATE", content[startingIndex + 8]);
        data.addEntry("LINES_OF_CODE", content[startingIndex + 9]);
        data.addEntry("FLOW_CONTROL_ELEMENT_COUNT", content[startingIndex + 10]);
    }
}
