package com.mizzle.blogrest.domain.mapping;

import java.time.LocalDateTime;
import java.util.Collection;

import com.mizzle.blogrest.domain.entity.blog.Tag;

public interface BoardMapping {
    Long getId();
    LocalDateTime getCreatedDate();
    String getTitle();
    String getSubtitle();
    String getUsername();
    String getMarkdown();
    String getHtml();
    Collection<Tag> getTag();
}
