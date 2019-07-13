package com.example.test.gsonUtil;

import java.util.List;

/**
 * created by 李军邑
 */
public class GetRepositoryOfUserBean {

    /**
     * errorCode : 0000
     * token : RmFuZ0t1blRlYW0=.Z2VuZXJhdGVUaW1lOjE1NjUyODI1NDg4NzY7.ODExNzJjYjg0YzUwMzZmZGYzYWUyNDQ3MDQ4M2VhNmM=
     * data : {"repositories":[1]}
     */

    private String errorCode;
    private String token;
    private DataBean data;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<Integer> repositories;

        public List<Integer> getRepositories() {
            return repositories;
        }

        public void setRepositories(List<Integer> repositories) {
            this.repositories = repositories;
        }
    }
}
