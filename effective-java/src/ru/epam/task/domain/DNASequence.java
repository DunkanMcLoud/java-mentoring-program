package ru.epam.task.domain;

public class DNASequence {

    private String sequence;

    public DNASequence(String sequence) {
        this.sequence = sequence;
    }

    public static DNASequence create(String sequence) {
        return new DNASequence(sequence);
    }

    public String getSequence() {
        return sequence;
    }
}
