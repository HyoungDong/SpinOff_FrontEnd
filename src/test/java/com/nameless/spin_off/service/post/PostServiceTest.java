package com.nameless.spin_off.service.post;

import com.nameless.spin_off.dto.PostDto;
import com.nameless.spin_off.entity.collections.CollectedPost;
import com.nameless.spin_off.entity.collections.Collection;
import com.nameless.spin_off.entity.collections.PublicOfCollectionStatus;
import com.nameless.spin_off.entity.member.Member;
import com.nameless.spin_off.entity.post.*;
import com.nameless.spin_off.exception.collection.NotExistCollectionException;
import com.nameless.spin_off.exception.member.NotExistMemberException;
import com.nameless.spin_off.exception.movie.NotExistMovieException;
import com.nameless.spin_off.exception.post.AlreadyLikedPostException;
import com.nameless.spin_off.exception.post.NotExistPostException;
import com.nameless.spin_off.exception.post.OverSearchLikedPostException;
import com.nameless.spin_off.exception.post.OverSearchViewedPostByIpException;
import com.nameless.spin_off.repository.collections.CollectionRepository;
import com.nameless.spin_off.repository.member.MemberRepository;
import com.nameless.spin_off.repository.post.HashtagRepository;
import com.nameless.spin_off.repository.post.LikedPostRepository;
import com.nameless.spin_off.repository.post.PostRepository;
import com.nameless.spin_off.repository.post.PostedMediaRepository;
import com.nameless.spin_off.service.comment.CommentInPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//@Rollback(value = false)
@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired PostService postService;
    @Autowired CommentInPostService commentInPostService;
    @Autowired PostRepository postRepository;
    @Autowired HashtagRepository hashtagRepository;
    @Autowired PostedMediaRepository postedMediaRepository;
    @Autowired CollectionRepository collectionRepository;
    @Autowired LikedPostRepository likedPostRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    public void 포스트_생성_테스트() throws Exception{
        //given
        Member member = Member.buildMember().build();
        memberRepository.save(member);

        collectionRepository.save(Collection.createDefaultCollection(member));
        collectionRepository.save(Collection.createCollection(member, "", "", PublicOfCollectionStatus.PUBLIC));
        collectionRepository.save(Collection.createCollection(member, "", "", PublicOfCollectionStatus.PUBLIC));

        List<Collection> collectionsByMember = collectionRepository.findAllByMember(member);

        List<Long> collectionIds = collectionsByMember.stream().map(Collection::getId).collect(Collectors.toList());

        List<Post> preparePosts = postRepository.findAll();
        List<Hashtag> prepareHashtags = hashtagRepository.findAll();
        List<Collection> prepareCollections = collectionRepository.findAllById(collectionIds);


        int preparePostsSize = preparePosts.size();
        int prepareHashtagsSize = prepareHashtags.size();
        List<CollectedPost> prepareCollectedPosts1 = prepareCollections.get(0).getCollectedPosts();
        List<CollectedPost> prepareCollectedPosts2 = prepareCollections.get(1).getCollectedPosts();
        List<CollectedPost> prepareCollectedPosts3 = prepareCollections.get(2).getCollectedPosts();

        int prepareCollectedPosts1Size = prepareCollectedPosts1.size();
        int prepareCollectedPosts2Size = prepareCollectedPosts2.size();
        int prepareCollectedPosts3Size = prepareCollectedPosts3.size();

        em.flush();
        em.clear();

        //when

        PostDto.CreatePostVO createPostVO = new PostDto.CreatePostVO(member.getId(),
                "알라리숑", "얄라리얄라", null, PublicOfPostStatus.PUBLIC,
                List.of("형윤이", "형윤이?"), List.of(), collectionIds);

        Long aLong = postService.insertPostByPostVO(createPostVO);
        Post post1 = postRepository.findById(aLong).orElseThrow(Exception::new);

        Long postCollectionCount = post1.getCollectionCount();
        List<PostedHashtag> postedHashtags = post1.getPostedHashtags();
        int postPostedHashtagSize = postedHashtags.size();

        List<Post> postPosts = postRepository.findAll();
        List<Hashtag> postHashtags = hashtagRepository.findAll();
        List<Collection> postCollections = collectionRepository.findAllById(collectionIds);

        int postPostsSize = postPosts.size();
        int postHashtagsSize = postHashtags.size();
        List<CollectedPost> postCollectedPosts1 = postCollections.get(0).getCollectedPosts();
        List<CollectedPost> postCollectedPosts2 = postCollections.get(1).getCollectedPosts();
        List<CollectedPost> postCollectedPosts3 = postCollections.get(2).getCollectedPosts();

        int postCollectedPosts1Size = postCollectedPosts1.size();
        int postCollectedPosts2Size = postCollectedPosts2.size();
        int postCollectedPosts3Size = postCollectedPosts3.size();

        //then

        assertThat(preparePostsSize).isEqualTo(postPostsSize - 1);
        assertThat(prepareHashtagsSize).isEqualTo(postHashtagsSize - 2);
        assertThat(prepareCollectedPosts1Size).isEqualTo(postCollectedPosts1Size - 1);
        assertThat(prepareCollectedPosts2Size).isEqualTo(postCollectedPosts2Size - 1);
        assertThat(prepareCollectedPosts3Size).isEqualTo(postCollectedPosts3Size - 1);
        assertThat(postCollectionCount).isEqualTo(3);
        assertThat(postPostedHashtagSize).isEqualTo(2);

    }

    @Test
    public void 글_생성_파라미터_예외처리() throws Exception{

        //given
        Member member = Member.buildMember().build();
        memberRepository.save(member);

        PostDto.CreatePostVO createPostVO1 = new PostDto.CreatePostVO(0L,
                "알라리숑", "얄라리얄라", null, PublicOfPostStatus.PUBLIC,
                List.of(), List.of(), List.of());

        PostDto.CreatePostVO createPostVO2 = new PostDto.CreatePostVO(member.getId(),
                "알라리숑", "얄라리얄라", 0L, PublicOfPostStatus.PUBLIC,
                List.of(), List.of(), List.of());

        PostDto.CreatePostVO createPostVO3 = new PostDto.CreatePostVO(member.getId(),
                "알라리숑", "얄라리얄라", null, PublicOfPostStatus.PUBLIC,
                List.of(), List.of(), List.of(0L));
        //when

        //then
        assertThatThrownBy(() -> postService.insertPostByPostVO(createPostVO1))
                .isInstanceOf(NotExistMemberException.class);//.hasMessageContaining("")

        assertThatThrownBy(() -> postService.insertPostByPostVO(createPostVO2))
                .isInstanceOf(NotExistMovieException.class);//.hasMessageContaining("")

        assertThatThrownBy(() -> postService.insertPostByPostVO(createPostVO3))
                .isInstanceOf(NotExistCollectionException.class);//.hasMessageContaining("")
    }

    @Test
    public void 글_좋아요_체크() throws Exception{

        //given
        Member mem = Member.buildMember().build();
        memberRepository.save(mem);
        Post po = Post.buildPost().setMember(mem).setPostPublicStatus(PublicOfPostStatus.PUBLIC).build();
        postRepository.save(po);

        em.flush();
        em.clear();

        //when
        System.out.println("서비스함수");
        postService.insertLikedPostByMemberId(mem.getId(), po.getId());

        System.out.println("포스트");
        Post post = postRepository.findById(po.getId()).get();
        System.out.println("멤버");
        Member member = memberRepository.findById(mem.getId()).get();

        //then

        List<LikedPost> likedPosts = post.getLikedPosts();
        Long likeCount = post.getLikeCount();
        assertThat(likeCount).isEqualTo(1);
        assertThat(likedPosts.size()).isEqualTo(1);
        assertThat(likedPosts.get(0).getMember()).isEqualTo(member);

    }

    @Test
    public void 글_좋아요_파라미터_예외처리() throws Exception{

        //given
        Member mem = Member.buildMember().build();
        memberRepository.save(mem);
        Post po = Post.buildPost().setMember(mem).setPostPublicStatus(PublicOfPostStatus.PUBLIC).build();
        postRepository.save(po);

        em.flush();
        em.clear();

        //when
        System.out.println("서비스함수");
        postService.insertLikedPostByMemberId(mem.getId(), po.getId());

        System.out.println("포스트");
        Post post = postRepository.findById(po.getId()).get();
        System.out.println("멤버");
        Member member = memberRepository.findById(mem.getId()).get();

        //then
        assertThatThrownBy(() -> postService.insertLikedPostByMemberId(0L, po.getId()))
                .isInstanceOf(NotExistMemberException.class);//.hasMessageContaining("")

        assertThatThrownBy(() -> postService.insertLikedPostByMemberId(mem.getId(), 0L))
                .isInstanceOf(NotExistPostException.class);//.hasMessageContaining("")

    }

    @Test
    public void 글_좋아요_중복삽입_예외처리() throws Exception{

        //given
        Member mem = Member.buildMember().build();
        memberRepository.save(mem);
        Post po = Post.buildPost().setMember(mem).setPostPublicStatus(PublicOfPostStatus.PUBLIC).build();
        postRepository.save(po);

        em.flush();
        em.clear();

        //when
        System.out.println("서비스함수");
        postService.insertLikedPostByMemberId(mem.getId(), po.getId());
        System.out.println("서비스함수2");

        //then
        System.out.println("서비스함수2");
        assertThatThrownBy(() -> postService.insertLikedPostByMemberId(mem.getId(), po.getId()))
                .isInstanceOf(AlreadyLikedPostException.class);//.hasMessageContaining("")

    }

    @Test
    public void 글_좋아요_복수데이터_예외처리() throws Exception{

        //given
        Member mem = Member.buildMember().build();
        memberRepository.save(mem);
        Post po = Post.buildPost().setMember(mem).setPostPublicStatus(PublicOfPostStatus.PUBLIC).build();
        postRepository.save(po);
        po.addLikedPostByMember(mem);
        po.addLikedPostByMember(mem);

        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> postService.insertLikedPostByMemberId(mem.getId(), po.getId()))
                .isInstanceOf(OverSearchLikedPostException.class);//.hasMessageContaining("")

    }

    @Test
    public void 글_조회수_증가() throws Exception{
        //given
        LocalDateTime now;
        Member member = Member.buildMember().build();
        memberRepository.save(member);
        Post post = Post.buildPost().setMember(member).setPostPublicStatus(PublicOfPostStatus.PUBLIC).build();
        postRepository.save(post);

        em.flush();
        em.clear();

        //when

        now = LocalDateTime.now();
        System.out.println("서비스함수");
        Post post1 = postService.insertViewedPostByIp("00", post.getId(), now, 60L);

        //then
        assertThat(post1.getViewCount()).isEqualTo(post1.getViewedPostByIps().size());
        assertThat(post1.getViewCount()).isEqualTo(1);

    }
    
    @Test
    public void 글_조회수_중복_체크() throws Exception{
        //given
        LocalDateTime now;
        Member member = Member.buildMember().build();
        memberRepository.save(member);
        Post post = Post.buildPost().setMember(member).setPostPublicStatus(PublicOfPostStatus.PUBLIC).build();
        postRepository.save(post);
        now = LocalDateTime.now();
        Post post1 = postService.insertViewedPostByIp("00", post.getId(), now, 60L);

        em.flush();
        em.clear();

        //when
        now = LocalDateTime.now();
        System.out.println("서비스함수");
        Post post2 = postService.insertViewedPostByIp("00", post.getId(), now, 60L);
        
        //then
        assertThat(post2.getViewCount()).isEqualTo(post2.getViewedPostByIps().size());
        assertThat(post2.getViewCount()).isEqualTo(1);

    }

    @Test
    public void 글_조회수_시간후_증가() throws Exception{

        //given
        LocalDateTime now;
        Member member = Member.buildMember().build();
        memberRepository.save(member);
        Post post = Post.buildPost().setMember(member).setPostPublicStatus(PublicOfPostStatus.PUBLIC).build();
        postRepository.save(post);
        now = LocalDateTime.now();
        Post post1 = postService.insertViewedPostByIp("00", post.getId(), now, 60L);

        em.flush();
        em.clear();

        //when
        now = LocalDateTime.now().plusHours(2);
        System.out.println("서비스함수");
        Post post2 = postService.insertViewedPostByIp("00", post.getId(), now, 60L);

        //then
        assertThat(post2.getViewCount()).isEqualTo(post2.getViewedPostByIps().size());
        assertThat(post2.getViewCount()).isEqualTo(2);

    }

    @Test
    public void 글_조회수_파리미터_AND_복수데이터_예외처리() throws Exception{

        //given
        LocalDateTime now;
        Member mem = Member.buildMember().build();
        memberRepository.save(mem);
        Post po = Post.buildPost().setMember(mem).setPostPublicStatus(PublicOfPostStatus.PUBLIC).build();
        postRepository.save(po);
        now = LocalDateTime.now();
        po.addViewedPostByIp("00");
        po.addViewedPostByIp("00");

        em.flush();
        em.clear();

        //when

        //then
        assertThatThrownBy(() -> postService.insertViewedPostByIp("00", 0L, now, 60L))
                .isInstanceOf(NotExistPostException.class);//.hasMessageContaining("")
        assertThatThrownBy(() -> postService.insertViewedPostByIp("00", po.getId(), now, 60L))
                .isInstanceOf(OverSearchViewedPostByIpException.class);//.hasMessageContaining("")

    }

}