package repository.dao.impl;

import repository.dao.entities.Literature;
import org.springframework.stereotype.Repository;

@Repository
public class LiteratureRepository extends AbstractRepository<Literature> {

    public LiteratureRepository() {
        super(Literature.class);
    }

}
