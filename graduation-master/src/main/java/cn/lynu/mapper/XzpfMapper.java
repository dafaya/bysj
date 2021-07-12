package cn.lynu.mapper;

import cn.lynu.model.Student;
import cn.lynu.model.Xzpf;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface XzpfMapper {

    int insertXzpf(Xzpf xzpf);

    int insertSelectiveXzpf(Xzpf xzpf);

    int updateScore(@Param("teacherId")String teacherId, @Param("studentId") String studentId,@Param("score") Integer score);

    int deleteXzpf(@Param("teacherId") String teacherId,@Param("studentId") String studentId);

    int deleteXzpfAll(@Param("studentId") String studentId);
    List<Xzpf> selectXzpfByStudentId(@Param("studentId") String studentId);

    Xzpf getXzpf(@Param("teacherId") String teacherId,@Param("studentId") String studentId);

    int updateXzpf(Xzpf xzpf);
}
