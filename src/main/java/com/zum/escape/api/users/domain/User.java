package com.zum.escape.api.users.domain;

import com.zum.escape.api.domain.entity.Problem;
import com.zum.escape.api.thirdPartyAdapter.leetcode.response.CrawledUserInfo;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "users")
@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id @GeneratedValue
    private Long id;
    private String userId;
    private int solvedQuestionCount;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserProblem> solvedProblem = new HashSet<>();

    public boolean checkSolveQuestion(CrawledUserInfo crawledUserInfo) {
        if(crawledUserInfo.solvedQuestion())
            return false;

        return this.solvedQuestionCount > crawledUserInfo.getSolvedQuestionCount();
    }

    public void setSolvedProblem(Set<UserProblem> solved) {
        this.solvedProblem = solved;
    }

    public void updateSolvedProblems(CrawledUserInfo crawledUserInfo) {
        if(crawledUserInfo.getProblems() == null) {
            System.out.println("NULL");
            return;
        }

        for(Problem problem : crawledUserInfo.getProblems()) {
            solvedProblem.add(new UserProblem(this, problem));
        }
    }
}
