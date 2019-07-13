package com.example.test.gsonUtil;

import java.util.List;

/**
 * created by 李军邑
 */
public class GetAllInfoBean {

    /**
     * errorCode : 0000
     * token : RmFuZ0t1blRlYW0=.Z2VuZXJhdGVUaW1lOjE1NjU0NDQ0ODczNDI7.ODExNzJjYjg0YzUwMzZmZGYzYWUyNDQ3MDQ4M2VhNmM=
     * data : {"details":[{"repository":{"r_id":1,"name":"kun的测试乐库","create_time":"2019-07-11T13:40:38.000+0000","introduce":"李军邑真可爱","is_public":1,"img":1,"type_1":1,"type_2":1,"type_3":1,"type_4":1},"files":[{"f_id":5,"filePath":"E:/data/4/1/long.wav","fileType":1,"load_time":"2019-07-11T13:41:21.000+0000","introduce":"大家好，我是练习两年半的练习生，蔡徐坤。","is_public":1}]}],"user":{"phone":"1234567","password":"fcea920f7412b5da7be0cf42b8c93759","email":null,"nickname":null,"signature":"81172cb84c5036fdf3ae24470483ea6c","img":null,"expired_time":"2019-08-10T13:41:21.000+0000","register_time":"2019-07-11T13:40:21.000+0000","uid":4}}
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
         * details : [{"repository":{"r_id":1,"name":"kun的测试乐库","create_time":"2019-07-11T13:40:38.000+0000","introduce":"李军邑真可爱","is_public":1,"img":1,"type_1":1,"type_2":1,"type_3":1,"type_4":1},"files":[{"f_id":5,"filePath":"E:/data/4/1/long.wav","fileType":1,"load_time":"2019-07-11T13:41:21.000+0000","introduce":"大家好，我是练习两年半的练习生，蔡徐坤。","is_public":1}]}]
         * user : {"phone":"1234567","password":"fcea920f7412b5da7be0cf42b8c93759","email":null,"nickname":null,"signature":"81172cb84c5036fdf3ae24470483ea6c","img":null,"expired_time":"2019-08-10T13:41:21.000+0000","register_time":"2019-07-11T13:40:21.000+0000","uid":4}
         */

        private UserBean user;
        private List<DetailsBean> details;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

        public static class UserBean {
            /**
             * phone : 1234567
             * password : fcea920f7412b5da7be0cf42b8c93759
             * email : null
             * nickname : null
             * signature : 81172cb84c5036fdf3ae24470483ea6c
             * img : null
             * expired_time : 2019-08-10T13:41:21.000+0000
             * register_time : 2019-07-11T13:40:21.000+0000
             * uid : 4
             */

            private String phone;
            private String password;
            private Object email;
            private Object nickname;
            private String signature;
            private Object img;
            private String expired_time;
            private String register_time;
            private int uid;

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public Object getEmail() {
                return email;
            }

            public void setEmail(Object email) {
                this.email = email;
            }

            public Object getNickname() {
                return nickname;
            }

            public void setNickname(Object nickname) {
                this.nickname = nickname;
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }

            public Object getImg() {
                return img;
            }

            public void setImg(Object img) {
                this.img = img;
            }

            public String getExpired_time() {
                return expired_time;
            }

            public void setExpired_time(String expired_time) {
                this.expired_time = expired_time;
            }

            public String getRegister_time() {
                return register_time;
            }

            public void setRegister_time(String register_time) {
                this.register_time = register_time;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }
        }

        public static class DetailsBean {
            /**
             * repository : {"r_id":1,"name":"kun的测试乐库","create_time":"2019-07-11T13:40:38.000+0000","introduce":"李军邑真可爱","is_public":1,"img":1,"type_1":1,"type_2":1,"type_3":1,"type_4":1}
             * files : [{"f_id":5,"filePath":"E:/data/4/1/long.wav","fileType":1,"load_time":"2019-07-11T13:41:21.000+0000","introduce":"大家好，我是练习两年半的练习生，蔡徐坤。","is_public":1}]
             */

            private RepositoryBean repository;
            private List<FilesBean> files;

            public RepositoryBean getRepository() {
                return repository;
            }

            public void setRepository(RepositoryBean repository) {
                this.repository = repository;
            }

            public List<FilesBean> getFiles() {
                return files;
            }

            public void setFiles(List<FilesBean> files) {
                this.files = files;
            }

            public static class RepositoryBean {
                /**
                 * r_id : 1
                 * name : kun的测试乐库
                 * create_time : 2019-07-11T13:40:38.000+0000
                 * introduce : 李军邑真可爱
                 * is_public : 1
                 * img : 1
                 * type_1 : 1
                 * type_2 : 1
                 * type_3 : 1
                 * type_4 : 1
                 */

                private int r_id;
                private String name;
                private String create_time;
                private String introduce;
                private int is_public;
                private Object img;
                private Object type_1;
                private Object type_2;
                private Object type_3;
                private Object type_4;

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

                public int getIs_public() {
                    return is_public;
                }

                public void setIs_public(int is_public) {
                    this.is_public = is_public;
                }

                public Object getImg() {
                    return img;
                }

                public void setImg(int img) {
                    this.img = img;
                }

                public Object getType_1() {
                    return type_1;
                }

                public void setType_1(int type_1) {
                    this.type_1 = type_1;
                }

                public Object getType_2() {
                    return type_2;
                }

                public void setType_2(int type_2) {
                    this.type_2 = type_2;
                }

                public Object getType_3() {
                    return type_3;
                }

                public void setType_3(int type_3) {
                    this.type_3 = type_3;
                }

                public Object getType_4() {
                    return type_4;
                }

                public void setType_4(int type_4) {
                    this.type_4 = type_4;
                }
            }

            public static class FilesBean {
                /**
                 * f_id : 5
                 * filePath : E:/data/4/1/long.wav
                 * fileType : 1
                 * load_time : 2019-07-11T13:41:21.000+0000
                 * introduce : 大家好，我是练习两年半的练习生，蔡徐坤。
                 * is_public : 1
                 */

                private int f_id;
                private String filePath;
                private int fileType;
                private String load_time;
                private String introduce;
                private int is_public;

                public int getF_id() {
                    return f_id;
                }

                public void setF_id(int f_id) {
                    this.f_id = f_id;
                }

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

                public String getLoad_time() {
                    return load_time;
                }

                public void setLoad_time(String load_time) {
                    this.load_time = load_time;
                }

                public String getIntroduce() {
                    return introduce;
                }

                public void setIntroduce(String introduce) {
                    this.introduce = introduce;
                }

                public int getIs_public() {
                    return is_public;
                }

                public void setIs_public(int is_public) {
                    this.is_public = is_public;
                }
            }
        }
    }
}
