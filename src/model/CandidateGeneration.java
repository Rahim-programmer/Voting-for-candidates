package model;

import FileService.FileService;

import java.util.ArrayList;
import java.util.List;

public class CandidateGeneration {
    private List<Candidate> candidates = new ArrayList<>();

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }
    public CandidateGeneration(){
        this.candidates = FileService.readFile();
        for(var candidate: candidates){
            candidate.setId(candidates.indexOf(candidate)+ 1);
            candidate.setVotes(0);
        }
    }
    }

