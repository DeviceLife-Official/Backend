package com.devicelife.devicelife_api.domain.content;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "faqs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faqId")
    private Long faqId;

    @Column(name = "question", length = 255, nullable = false)
    private String question;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "sortOrder", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(name = "isPublished", nullable = false)
    @Builder.Default
    private boolean isPublished = false;

    public void update(String question, String answer, Integer sortOrder, boolean isPublished) {
        this.question = question;
        this.answer = answer;
        this.sortOrder = sortOrder;
        this.isPublished = isPublished;
    }
}

