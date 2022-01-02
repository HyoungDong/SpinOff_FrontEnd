package com.nameless.spin_off.domain;

import com.nameless.spin_off.domain.member.Member;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DirectMessage {

    @Id
    @GeneratedValue
    @Column(name="directmessage_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receivemember_id")
    @NotNull
    private Member receivedMember;

    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //==연관관계 메소드==//

    //==생성 메소드==//
    public static DirectMessage createDirectMessage(Member member, Member receivedMember, String content) {

        DirectMessage directMessage = new DirectMessage();
        directMessage.updateMember(member);
        directMessage.updateReceivedMember(member);
        directMessage.updateContent(content);
        directMessage.updateCreatedAtNow();

        return directMessage;

    }

    //==수정 메소드==//
    private void updateCreatedAtNow() {
        this.createdAt = LocalDateTime.now();
    }

    private void updateContent(String content) {
        this.content = content;
    }

    private void updateReceivedMember(Member member) {
        this.receivedMember = member;
    }

    private void updateMember(Member member) {
        this.member = member;
    }

    //==비즈니스 로직==//

    //==조회 로직==//

}
