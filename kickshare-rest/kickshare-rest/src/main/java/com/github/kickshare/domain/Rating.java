package com.github.kickshare.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Jan.Kucera
 * @since 12.1.2018
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @NotNull
    @JsonProperty("author_id")
    private Long authorId;

    @NotNull
    @JsonProperty("group_id")
    private Long groupId;

    @NotNull
    @JsonProperty("backer_id")
    private Long backerId;

    @NonNull
    @Min(0)
    @Max(5)
    private Short rating;

    @Size(min = 1, max = 5000)
    private String message;
}
