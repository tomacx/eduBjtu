package com.example.edubjtu.repository;

import com.example.edubjtu.dto.studentHomeWork;
import com.example.edubjtu.model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    // 使用自定义查询来获取每个作业编号的第一个作业
    @Query("SELECT h FROM Homework h WHERE h.courseId = :courseId AND h.homeworkId = (" +
            "  SELECT MIN(h2.homeworkId) FROM Homework h2 " +
            "  WHERE h2.courseId = :courseId AND h2.homeworkNum = h.homeworkNum " +
            "  AND h2.submissionDeadline = (SELECT MIN(h3.submissionDeadline) FROM Homework h3 " +
            "                                WHERE h3.courseId = :courseId AND h3.homeworkNum = h2.homeworkNum)" +
            ")")
    List<Homework> findFirstHomeworkByCourseId(@Param("courseId") Long courseId);

    Homework findByHomeworkId(@Param("homeworkId")Long homeworkId);

    @Query("SELECT new com.example.edubjtu.dto.studentHomeWork( " +
            "h.homeworkId, " +
            "h.courseId, " +
            "h.studentNum, " +
            "h.grade, " +
            "h.avgGrade, " +
            "h.mutualGrade, " +
            "h.content, " +
            "h.homeworkNum, " +
            "h.submissionDeadline, " +
            "h.studentContent, " +
            "CASE WHEN (r.homework.homeworkId  || h.studentContent) IS NOT NULL THEN true ELSE false END ) " +
            "FROM Homework h " +
            "LEFT JOIN Resource r ON h.homeworkId = r.homework.homeworkId " +
            "WHERE h.studentNum = :studentNum AND h.courseId = :courseId")
    List<studentHomeWork> findStudentHomeWorkByCourseIdAndStudentNum(@Param("courseId")Long courseId,@Param("studentNum") String studentNum);

    Optional<Homework> findByHomeworkNumAndStudentNum(@Param("homework_num")Integer homeworkNum,@Param("student_num") String studentNum);

    List<Homework> findByCourseIdAndStudentNum(@Param("courseId")Long courseId, @Param("studentNum")String studentNum);

    List<Homework> findByCourseId(@Param("courseId")Long courseId);

}
