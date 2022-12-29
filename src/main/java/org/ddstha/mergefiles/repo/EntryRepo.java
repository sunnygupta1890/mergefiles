package org.ddstha.mergefiles.repo;

import org.ddstha.mergefiles.model.Entry;
import org.ddstha.mergefiles.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EntryRepo extends MongoRepository<Entry, String> {
    List<Entry> findByFile(File file);
}