package com.seniorproject.first.prototype.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostParticipateRequest {
    private Map<Integer, String> participantResponse;
}
