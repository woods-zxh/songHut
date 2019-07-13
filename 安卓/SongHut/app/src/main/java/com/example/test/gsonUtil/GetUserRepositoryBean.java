package com.example.test.gsonUtil;

import java.util.List;

/**
 * created by 李军邑
 */
public class GetUserRepositoryBean {

    /**
     * errorCode : 0000
     * token : RmFuZ0t1blRlYW0=.Z2VuZXJhdGVUaW1lOjE1NjQzNjYyMDQ5NzQ7.ZGI2ZWRiNDQ3ZjI3NzgxNGRlN2FlNGJhNTM5NGIyM2Y=
     * data : {"repositories":[]}
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
        private List<?> repositories;

        public List<?> getRepositories() {
            return repositories;
        }

        public void setRepositories(List<?> repositories) {
            this.repositories = repositories;
        }
    }
}
