package com.example.edubjtu.repository;

import com.example.edubjtu.dto.ResourceList;
import com.example.edubjtu.model.Homework;
import com.example.edubjtu.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {


    List<Resource> findByCourse_CourseId(Long courseId);

    @Query(value = """
            select new com.example.edubjtu.dto.ResourceList(p.id,p.course.courseId,p.filePath,p.fileType) \
            from Resource p
            where p.course.courseId=:courseId
            and p.courseResource=:courseResource""")
    List<ResourceList> findByCourseIdAndCourseResource(@Param("courseId") Long courseId, @Param("courseResource") int courseResource);

    @Query(value = """
            select new com.example.edubjtu.dto.ResourceList(p.id,p.course.courseId,p.filePath,p.fileType) \
            from Resource p
            where p.course.courseId=:courseId
            and p.courseWorkset=:courseWorkset""")
    List<ResourceList> findByCourseIdAndCourseWorkSet(@Param("courseId") Long courseId, @Param("courseWorkset") int courseWorkset);

    Optional<Resource> findOutLineByCourse_CourseIdAndCourseOutline(@Param("courseId")Long courseId, @Param("courseOutline")int courseOutline);

    Optional<Resource> findCalendarByCourse_CourseIdAndCourseCalendar(@Param("courseId")Long courseId,@Param("courseCalendar")int courseCalendar);

    void deleteByHomework(Homework homework);

    void deleteById(Long id);
    @Query(value= """
           select r from Resource r where r.homework.homeworkId = :homeworkId
        """)
    Resource findByHomeworkId(@Param("homeworkId")Long homeworkId);
@Query(value = """
    select r from Resource r where r.course.courseId = :courseId and r.homeworkResource=:homeworkNum
""")
    Optional<Resource> findHomeworkAttachement(Long courseId, Long homeworkNum);
}
