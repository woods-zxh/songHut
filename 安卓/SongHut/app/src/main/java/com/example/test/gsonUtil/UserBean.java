package com.example.test.gsonUtil;

import java.util.List;


/**
 * created by 李军邑
 */
public class UserBean {
    /**
     * phone : 123456789
     * password : 123456789
     * token : RmFuZ0t1blRlYW0=.Z2VuZXJhdGVUaW1lOjE1NjQzNjUyOTk2MDE7.N2IyMzIzZDMxZDhlMzU0OWRlOWEzZjBjNzZiMGMwNTg=
     * uId : 1
     * name : 不呐呐
     * introduce : 这个用户很懒，什么都没留下
     * repository : [{"rId":1,"rName":"不呐呐呐","rCreateTime":"2019-07-09T16:38:29.000+0000","rIntroduce":"这个用户很懒，什么都没留下","rIsPublic":1,"rImageContent":"SDPath","rImagePath":5,"type1":1,"type2":2,"type3":3,"type4":4,"files":[{"file":{"fId":"00001","fName":"不呐呐呐呐","fCreateTime":"2019-07-09T16:38:29.000+0000","fIntroduce":"这个用户很懒，什么都没留下","fType":1,"fContent":"path","fPath":"E:/data/4/1/long.wav"}}]}]
     */

    private String phone;
    private String password;
    private String token;
    private int uId;
    private String name;
    private String introduce;
    private List<RepositoryBean> repository;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUId() {
        return uId;
    }

    public void setUId(int uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public List<RepositoryBean> getRepository() {
        return repository;
    }

    public void setRepository(List<RepositoryBean> repository) {
        this.repository = repository;
    }

    public static class RepositoryBean {
        /**
         * rId : 1
         * rName : 不呐呐呐
         * rCreateTime : 2019-07-09T16:38:29.000+0000
         * rIntroduce : 这个用户很懒，什么都没留下
         * rIsPublic : 1
         * rImageContent : SDPath
         * rImagePath : 5
         * type1 : 1
         * type2 : 2
         * type3 : 3
         * type4 : 4
         * files : [{"file":{"fId":"00001","fName":"不呐呐呐呐","fCreateTime":"2019-07-09T16:38:29.000+0000","fIntroduce":"这个用户很懒，什么都没留下","fType":1,"fContent":"path","fPath":"E:/data/4/1/long.wav"}}]
         */

        private int rId;
        private String rName;
        private String rCreateTime;
        private String rIntroduce;
        private int rIsPublic;
        private String rImageContent;
        private int rImagePath;
        private int type1;
        private int type2;
        private int type3;
        private int type4;
        private List<FilesBean> files;

        public int getRId() {
            return rId;
        }

        public void setRId(int rId) {
            this.rId = rId;
        }

        public String getRName() {
            return rName;
        }

        public void setRName(String rName) {
            this.rName = rName;
        }

        public String getRCreateTime() {
            return rCreateTime;
        }

        public void setRCreateTime(String rCreateTime) {
            this.rCreateTime = rCreateTime;
        }

        public String getRIntroduce() {
            return rIntroduce;
        }

        public void setRIntroduce(String rIntroduce) {
            this.rIntroduce = rIntroduce;
        }

        public int getRIsPublic() {
            return rIsPublic;
        }

        public void setRIsPublic(int rIsPublic) {
            this.rIsPublic = rIsPublic;
        }

        public String getRImageContent() {
            return rImageContent;
        }

        public void setRImageContent(String rImageContent) {
            this.rImageContent = rImageContent;
        }

        public int getRImagePath() {
            return rImagePath;
        }

        public void setRImagePath(int rImagePath) {
            this.rImagePath = rImagePath;
        }

        public int getType1() {
            return type1;
        }

        public void setType1(int type1) {
            this.type1 = type1;
        }

        public int getType2() {
            return type2;
        }

        public void setType2(int type2) {
            this.type2 = type2;
        }

        public int getType3() {
            return type3;
        }

        public void setType3(int type3) {
            this.type3 = type3;
        }

        public int getType4() {
            return type4;
        }

        public void setType4(int type4) {
            this.type4 = type4;
        }

        public List<FilesBean> getFiles() {
            return files;
        }

        public void setFiles(List<FilesBean> files) {
            this.files = files;
        }

        public static class FilesBean {
            /**
             * file : {"fId":"00001","fName":"不呐呐呐呐","fCreateTime":"2019-07-09T16:38:29.000+0000","fIntroduce":"这个用户很懒，什么都没留下","fType":1,"fContent":"path","fPath":"E:/data/4/1/long.wav"}
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
                 * fId : 00001
                 * fName : 不呐呐呐呐
                 * fCreateTime : 2019-07-09T16:38:29.000+0000
                 * fIntroduce : 这个用户很懒，什么都没留下
                 * fType : 1
                 * fContent : path
                 * fPath : E:/data/4/1/long.wav
                 */

                private int fId;
                private String fName;
                private String fCreateTime;
                private String fIntroduce;
                private int fType;
                private String fContent;
                private String fPath;

                public int getFId() {
                    return fId;
                }

                public void setFId(int fId) {
                    this.fId = fId;
                }

                public String getFName() {
                    return fName;
                }

                public void setFName(String fName) {
                    this.fName = fName;
                }

                public String getFCreateTime() {
                    return fCreateTime;
                }

                public void setFCreateTime(String fCreateTime) {
                    this.fCreateTime = fCreateTime;
                }

                public String getFIntroduce() {
                    return fIntroduce;
                }

                public void setFIntroduce(String fIntroduce) {
                    this.fIntroduce = fIntroduce;
                }

                public int getFType() {
                    return fType;
                }

                public void setFType(int fType) {
                    this.fType = fType;
                }

                public String getFContent() {
                    return fContent;
                }

                public void setFContent(String fContent) {
                    this.fContent = fContent;
                }

                public String getFPath() {
                    return fPath;
                }

                public void setFPath(String fPath) {
                    this.fPath = fPath;
                }
            }
        }
    }
}
