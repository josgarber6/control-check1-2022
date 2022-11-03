package org.springframework.samples.petclinic.feeding;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedingRepository extends CrudRepository<Feeding, Integer> {
    List<Feeding> findAll();
    
    @Query("SELECT ft FROM FeedingType ft")
    public List<FeedingType> findAllFeedingTypes();
    Optional<Feeding> findById(int id);
    Feeding save(Feeding p);

    @Query("SELECT ft FROM FeedingType ft WHERE ft.name = :name")
    public FeedingType findTypeByName(@Param("name") String name);

    // @Query("SELECT ft FROM FeedingType ft WHERE ft.id = :id")
    // public FeedingType findTypeById(@Param("id") Integer id);

}
