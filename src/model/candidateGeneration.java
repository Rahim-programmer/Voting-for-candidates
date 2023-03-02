package model;

import java.util.ArrayList;
import java.util.List;

public class candidateGeneration {
    private List<Candidate> candidates = new ArrayList<>();

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }
}
