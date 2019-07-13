package com.example.test.gsonUtil;

import java.util.List;

/**
 * created by 李军邑
 */
public class GetRepositoryInfoBean {

    /**
     * errorCode : 0000
     * token : RmFuZ0t1blRlYW0=.Z2VuZXJhdGVUaW1lOjE1NjUyODQ3MjI2ODk7.ODExNzJjYjg0YzUwMzZmZGYzYWUyNDQ3MDQ4M2VhNmM=
     * data : {"files":[2,2],"repository":{"r_id":1,"name":"kun的测试乐库","create_time":"2019-07-09T16:38:29.000+0000","introduce":"贺宇阳真可爱","is_public":null,"img":null,"type_1":null,"type_2":null,"type_3":null,"type_4":null,"createTime":"2019-07-09T16:38:29.000+0000","rId":1}}
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
         * files : [2,2]
         * repository : {"r_id":1,"name":"kun的测试乐库","create_time":"2019-07-09T16:38:29.000+0000","introduce":"贺宇阳真可爱","is_public":null,"img":null,"type_1":null,"type_2":null,"type_3":null,"type_4":null,"createTime":"2019-07-09T16:38:29.000+0000","rId":1}
         */

        private RepositoryBean repository;
        private List<Integer> files;

        public RepositoryBean getRepository() {
            return repository;
        }

        public void setRepository(RepositoryBean repository) {
            this.repository = repository;
        }

        public List<Integer> getFiles() {
            return files;
        }

        public void setFiles(List<Integer> files) {
            this.files = files;
        }

        public static class RepositoryBean {
            /**
             * r_id : 1
             * name : kun的测试乐库
             * create_time : 2019-07-09T16:38:29.000+0000
             * introduce : 贺宇阳真可爱
             * is_public : null
             * img : null
             * type_1 : null
             * type_2 : null
             * type_3 : null
             * type_4 : null
             * createTime : 2019-07-09T16:38:29.000+0000
             * rId : 1
             */

            private int r_id;
            private String name;
            private String create_time;
            private String introduce;
            private Object is_public;
            private Object img;
            private Object type_1;
            private Object type_2;
            private Object type_3;
            private Object type_4;
            private String createTime;
            private int rId;

            public int getR_id() {
                return r_id;
            }

            public void setR_id(int r_id) {
                this.r_id = r_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
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

            public Object getImg() {
                return img;
            }

            public void setImg(Object img) {
                this.img = img;
            }

            public Object getType_1() {
                return type_1;
            }

            public void setType_1(Object type_1) {
                this.type_1 = type_1;
            }

            public Object getType_2() {
                return type_2;
            }

            public void setType_2(Object type_2) {
                this.type_2 = type_2;
            }

            public Object getType_3() {
                return type_3;
            }

            public void setType_3(Object type_3) {
                this.type_3 = type_3;
            }

            public Object getType_4() {
                return type_4;
            }

            public void setType_4(Object type_4) {
                this.type_4 = type_4;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getRId() {
                return rId;
            }

            public void setRId(int rId) {
                this.rId = rId;
            }
        }
    }
}
