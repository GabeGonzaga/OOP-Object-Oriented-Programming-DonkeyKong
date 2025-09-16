package pt.iscte.poo.game;

import java.io.*;
import java.util.*;

public class LeaderBoard {

    private File file;

    public LeaderBoard() {
        
        file = new File("leaderboard.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addScore(String name, int time) {
        try {
            Map<String, Integer> scores = new HashMap<>();

            // Read current scores from the file
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 2) {
                        String existingName = parts[0].trim();
                        int existingTime = Integer.parseInt(parts[1].trim());
                        scores.put(existingName, existingTime);
                    }
                }
            }

            // Update the score if the name exists and the new time is better
            if (scores.containsKey(name)) {
                if (time < scores.get(name)) {
                    scores.put(name, time);
                }
            } else {
                // Add new score
                scores.put(name, time);
            }

            // Write updated scores back to the file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                    bw.write(entry.getKey() + ";" + entry.getValue());
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getScores() {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<String[]> scoreList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    scoreList.add(parts);
                }
            }

            // Sort scores by time (ascending)
            scoreList.sort((a, b) -> Integer.compare(Integer.parseInt(a[1].trim()), Integer.parseInt(b[1].trim())));

            // Get top 10 scores
            int limit = Math.min(10, scoreList.size());
            for (int i = 0; i < limit; i++) {
                String[] entry = scoreList.get(i);
                sb.append("Nome: ").append(entry[0].trim()).append("; Tempo: ").append(entry[1].trim()).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}