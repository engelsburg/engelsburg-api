package io.github.paexception.engelsburg.api.database.repository;


import io.github.paexception.engelsburg.api.database.model.GradeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface GradeRepository extends JpaRepository<GradeModel, Integer> {

	void deleteAllByUserId(UUID userId);

	List<GradeModel> findAllByUserId(UUID userId);

	Stream<GradeModel> findAllByUserIdAndSubject(UUID userId, String subject);

}
