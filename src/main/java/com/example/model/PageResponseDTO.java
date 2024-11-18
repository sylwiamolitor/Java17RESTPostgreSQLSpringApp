package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageResponseDTO<RegionAndSubregionDTO> {
    private List<RegionAndSubregionDTO> pagedContent;
    private int totalSize;
    private int pageSize;
    private int offset;
    private String sortBy;
}