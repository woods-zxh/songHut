package com.songhut.songhut.model;

import java.util.List;

/**
 * 乐库详细信息模型
 * @author Kun
 */
public class RepositoryDetail {
    private Repository repository;
    private List<File> files;

    public RepositoryDetail(Repository repository, List<File> files) {
        this.repository = repository;
        this.files = files;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
