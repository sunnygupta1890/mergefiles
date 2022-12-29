package org.ddstha.mergefiles.repo;

import org.ddstha.mergefiles.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface FileRepo extends MongoRepository<File, String> {

}
