package io.github.charlesvall.mobamatch.infrastructure.entity;

import io.github.charlesvall.mobamatch.domain.model.Region;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private List<String> playerIds;

    @Column(nullable = false)
    @Min(0) @Max(100)
    private int averageSkill;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region;
}

/*
{
    "playerIds": [
        "bb512abd-361e-404d-a1f9-8ffd0857e820",
        "3bb3a9ff-9527-4a1a-94a8-683aaab92d12",
        "752d639c-424e-49ba-b239-c1608b31f9f4",
        "f7c7385e-acaa-46c1-b1ed-bf4cff189bad",
        "557f0eba-debc-41b8-854a-9a92503438b7",

        "d0fd7d49-9893-4068-9996-0bf012a742f8",
        "7b4def2f-2d90-4d58-9e5c-0fdf5b819ba8",
        "641c8eb2-5900-467b-af20-99ea960d7422",
        "8297a4e5-fff7-437d-8507-444f567b9075",
        "b3f22123-0f23-461c-b960-ce856b7e337d",
    ]
}
*/