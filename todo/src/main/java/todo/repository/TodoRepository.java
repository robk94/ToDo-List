package todo.repository;

import java.util.List;



import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import todo.entity.Todo;

@Repository
@Transactional
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByTitleContainingIgnoreCase(String keyword);

    @Query("UPDATE Todo t SET t.completed = :completed WHERE t.id = :id")
    @Modifying
    public void updateCompletedStatus(Integer id, boolean completed);
}
