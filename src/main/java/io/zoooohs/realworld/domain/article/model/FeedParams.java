package io.zoooohs.realworld.domain.article.model;

import lombok.*;

import javax.validation.constraints.AssertTrue;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedParams {
    private Integer offset;
    private Integer limit;

    @AssertTrue
    private boolean getValidPage() {
        return (offset != null && limit != null) || (offset == null && limit == null);
    }
}
