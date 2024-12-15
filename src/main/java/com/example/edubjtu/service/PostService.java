package com.example.edubjtu.service;

import com.example.edubjtu.dto.CoursePost;
import com.example.edubjtu.model.Favorite;
import com.example.edubjtu.model.Post;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FavoriteInfoService favoriteInfoService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private StudentService studentService;

    public List<CoursePost> getPostsByCourseId(Long courseId) {
        return postRepository.findByCourseId(courseId);
    }

    public List<CoursePost> getPostByPostId(Long postId) {

        return postRepository.findCoursePostById(postId);
    }

    public List<Post> getPostsByStudentId(Long studentId) {
        return postRepository.findByStudentId(studentId);
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }

    public void deletePost(Long postId) {
        postRepository.deletePostByPostId(postId);
    }

    public List<Post> searchPostsByTitle(String title) {
        return postRepository.findByTitleContaining(title);
    }

    public void likePost(Long postId) {
        postRepository.incrementLikes(postId);
    }

    public void addLikeNumById(Long postId) {
        Post post = postRepository.findByPostId(postId);
        Long likeNum = post.getLikeNum();
        post.setLikeNum(++likeNum);
        postRepository.save(post);
    }

    public void DecreaseLikeNumById(Long postId) {
        Post post = postRepository.findByPostId(postId);
        Long likeNum = post.getLikeNum();
        post.setLikeNum(--likeNum);
        postRepository.save(post);
    }

    public void addFavoNum(Long postId, String studentNum) {
        Post post = postRepository.findByPostId(postId);
        Long favoNum = post.getFavoNum();
        post.setFavoNum(++favoNum);
        postRepository.save(post);

        Student student=studentService.getStudentByStudentNum(studentNum);
        String favorite_num = favoriteInfoService.findByCreaterId(student.getId()).getFavouriteNum();
       favoriteService.addFavoritePost(postId, favorite_num);
    }

    public void deFavoNum(Long postId, String studentNum) {
        Post post = postRepository.findByPostId(postId);
        Long favoNum = post.getFavoNum();
        post.setFavoNum(--favoNum);
        postRepository.save(post);

        Student student=studentService.getStudentByStudentNum(studentNum);
        String favorite_num = favoriteInfoService.findByCreaterId(student.getId()).getFavouriteNum();
        Optional<Favorite> favorite=favoriteService.findByPostIdAndFavoriteNum(postId,favorite_num);
        favoriteService.deleteFavoPostByFavoId(favorite.get().getFavoriteId());
    }
}
