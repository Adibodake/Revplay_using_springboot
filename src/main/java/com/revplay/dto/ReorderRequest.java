package com.revplay.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReorderRequest {
    @NotEmpty
    private List<Long> songIds; // desired order
}
