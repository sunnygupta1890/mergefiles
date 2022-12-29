package org.ddstha.mergefiles.service;

import org.ddstha.mergefiles.model.Entry;
import org.ddstha.mergefiles.model.File;
import org.ddstha.mergefiles.repo.EntryRepo;
import org.ddstha.mergefiles.repo.FileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileEntryService {

    @Autowired
    private EntryRepo entryRepo;

    @Autowired
    private FileRepo fileRepo;

    public void persistEntry(Entry entry, String fileId){
        File file = entry.getFileId();
        file.setFileId(fileId);
        entryRepo.save(entry);
    }

    public String persistFile(File file){
        return fileRepo.save(file).getFileId();
    }

    public List<Entry> fetchAllEntries(List<String> fileIds){
        List<Entry> entries = new ArrayList<>();
        for (String fileId: fileIds){
            File file = new File();
            file.setFileId(fileId);
            List<Entry> entries1 = entryRepo.findByFile(file);
            entries.addAll(entries1);
            System.out.println("entries..."+entries.size());
        }
        return entries;
    }
}
