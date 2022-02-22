package com.nameless.spin_off.repository.comment;

import com.nameless.spin_off.entity.collection.Collection;
import com.nameless.spin_off.entity.comment.CommentInCollection;
import com.nameless.spin_off.entity.member.Member;
import com.nameless.spin_off.repository.collection.CollectedPostRepository;
import com.nameless.spin_off.repository.collection.CollectionRepository;
import com.nameless.spin_off.repository.member.MemberRepository;
import com.nameless.spin_off.repository.hashtag.HashtagRepository;
import com.nameless.spin_off.repository.post.PostRepository;
import com.nameless.spin_off.service.comment.CommentInCollectionService;
import com.nameless.spin_off.service.post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentInCollectionRepositoryTest {
    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CollectionRepository collectionRepository;
    @Autowired CollectedPostRepository collectedPostRepository;
    @Autowired HashtagRepository hashtagRepository;
    @Autowired PostService postService;
    @Autowired CommentInCollectionRepository commentInCollectionRepository;
    @Autowired EntityManager em;
    @Autowired CommentInCollectionService commentInCollectionService;


    @Test
    public void 부모댓글_찾기() throws Exception{
        //given
        Member member = Member.buildMember().build();
        memberRepository.save(member);
        Collection collection = Collection.createDefaultCollection(member);
        collectionRepository.save(collection);
        CommentInCollection parentComment = CommentInCollection
                .createCommentInCollection(member, "야스히로 라할살", null);
        collection.addCommentInCollection(parentComment);

        CommentInCollection childComment1 = CommentInCollection
                .createCommentInCollection(member, "요지스타 라할살", parentComment);
        CommentInCollection childComment2 = CommentInCollection
                .createCommentInCollection(member, "슈퍼스타검흰 라할살", parentComment);

        em.flush();
        collection.addCommentInCollection(childComment1);
        em.flush();
        collection.addCommentInCollection(childComment2);
        em.flush();
        em.clear();

        //when
        System.out.println("레포지토리함수");
        List<CommentInCollection> list = commentInCollectionRepository
                .findParentsByCollectionIdWithChildren(collection);

        System.out.println("컬렉션");
        collection = collectionRepository.getById(collection.getId());
        System.out.println("멤버");
        member = memberRepository.getById(member.getId());
        //then
        for (CommentInCollection comment : list) {
            assertThat(comment.getParent()).isNull();
            assertThat(comment.getCollection()).isEqualTo(collection);
            assertThat(comment.getMember()).isEqualTo(member);
            assertThat(comment.getChildren().size()).isEqualTo(2);

            for (CommentInCollection child : comment.getChildren()) {
                assertThat(child.getParent()).isEqualTo(comment);
                assertThat(child.getCollection()).isEqualTo(collection);
                assertThat(child.getMember()).isEqualTo(member);

            }
        }

    }

    @Test
    public void findParentsByCollectionIncludeChildrenOrderByDesc() throws Exception{
        //given
        Member member = Member.buildMember().build();
        memberRepository.save(member);
        Collection collection = Collection.createDefaultCollection(member);
        collectionRepository.save(collection);
        CommentInCollection parentComment = CommentInCollection
                .createCommentInCollection(member, "야스히로 라할살", null);
        collection.addCommentInCollection(parentComment);

        CommentInCollection childComment1 = CommentInCollection
                .createCommentInCollection(member, "요지스타 라할살", parentComment);
        CommentInCollection childComment2 = CommentInCollection
                .createCommentInCollection(member, "슈퍼스타검흰 라할살", parentComment);

        em.flush();
        collection.addCommentInCollection(childComment1);

        em.flush();
        collection.addCommentInCollection(childComment2);

        //when
        List<CommentInCollection> list = commentInCollectionRepository
                .findParentsByCollectionIdWithChildren(collection);

        //then
        for (CommentInCollection comment : list) {
            assertThat(comment.getParent()).isNull();
            assertThat(comment.getCollection()).isEqualTo(collection);
            assertThat(comment.getMember()).isEqualTo(member);
            assertThat(comment.getChildren().size()).isEqualTo(2);

            for (CommentInCollection child : comment.getChildren()) {
                assertThat(child.getParent()).isEqualTo(comment);
                assertThat(child.getCollection()).isEqualTo(collection);
                assertThat(child.getMember()).isEqualTo(member);

            }
        }

    }

}