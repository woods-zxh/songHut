package com.example.test.gsonUtil;


/**
 * created by 李军邑
 */
public class GetFileInfoBean {

    /**
     * errorCode : 0000
     * token : RmFuZ0t1blRlYW0=.Z2VuZXJhdGVUaW1lOjE1NjUyODUwNjEzNTU7.ODExNzJjYjg0YzUwMzZmZGYzYWUyNDQ3MDQ4M2VhNmM=
     * data : {"file":{"filePath":"E:/data/4/1/long.wav","fileType":1,"introduce":"大家好，我是练习两年半的练习生，蔡徐坤。","is_public":null,"loadTime":"2019-07-09T16:40:03.000+0000","fId":2}}
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
         * file : {"filePath":"E:/data/4/1/long.wav","fileType":1,"introduce":"大家好，我是练习两年半的练习生，蔡徐坤。","is_public":null,"loadTime":"2019-07-09T16:40:03.000+0000","fId":2}
         */

        private FileBean file;

        public FileBean getFile() {
            return file;
        }

        public void setFile(FileBean file) {
            this.file = file;
        }

        public static class FileBean {
            /**
             * filePath : E:/data/4/1/long.wav
             * fileType : 1
             * introduce : 大家好，我是练习两年半的练习生，蔡徐坤。
             * is_public : null
             * loadTime : 2019-07-09T16:40:03.000+0000
             * fId : 2
             */

            private String filePath;
            private int fileType;
            private String introduce;
            private Object is_public;
            private String loadTime;
            private int fId;

            public String getFilePath() {
                return filePath;
            }

            public void setFilePath(String filePath) {
                this.filePath = filePath;
            }

            public int getFileType() {
                return fileType;
            }

            public void setFileType(int fileType) {
                this.fileType = fileType;
            }

            public String getIntroduce() {
                return introduce;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }

            public Object getIs_public() {
                return is_public;
            }

            public void setIs_public(Object is_public) {
                this.is_public = is_public;
            }

            public String getLoadTime() {
                return loadTime;
            }

            public void setLoadTime(String loadTime) {
                this.loadTime = loadTime;
            }

            public int getFId() {
                return fId;
            }

            public void setFId(int fId) {
                this.fId = fId;
            }
        }
    }
}
