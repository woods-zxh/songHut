package com.example.test.gsonUtil;


/**
 * created by 李军邑
 */
public class PostFileToRepositoryBean {

    /**
     * errorCode : 0000
     * token : RmFuZ0t1blRlYW0=.Z2VuZXJhdGVUaW1lOjE1NjQzNjYwMzgwMTk7.N2IyMzIzZDMxZDhlMzU0OWRlOWEzZjBjNzZiMGMwNTg=
     * data : {"fid":9}
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
        /**
         * fid : 9
         */

        private int fid;

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
        }
    }
}
