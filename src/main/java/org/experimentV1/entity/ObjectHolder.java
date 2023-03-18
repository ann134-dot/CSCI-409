package org.experimentV1.entity;

import lombok.Data;

import java.util.List;

@Data
public class ObjectHolder {
    Experiment experiment;
    List<Integer> numberOfWordsPerLength;
}
