package com.example.test.songhut;

/**
 * created by 卢羽帆
 */
public class Details {

    private Repository repository;
    private Files[] files;

    public Repository getRepository(){
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Files[] getFiles(){
        return files;
    }

    public void setFiles(Files[] files) {
        this.files = files;
    }
}

class Repository{

    private int r_id;
    private String name;
    private String create_time;
    private String introduce;
    private int is_public;
    private int img;
    private int type_1;
    private int type_2;
    private int type_3;
    private int type_4;


    public int getR_id() {
        return r_id;
    }

    public void setR_id(int r_id) {
        this.r_id = r_id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getCreate_time(){
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIntroduce(){
        return introduce;
    }

    public void setIntroduce(String introduce){
        this.introduce = introduce;
    }

    public int getIs_public() {
        return is_public;
    }

    public void setIs_public(int is_public) {
        this.is_public = is_public;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getType_1() {
        return type_1;
    }

    public void setType_1(int type_1) {
        this.type_1 = type_1;
    }

    public int getType_2() {
        return type_2;
    }

    public void setType_2(int type_2) {
        this.type_2 = type_2;
    }

    public int getType_3() {
        return type_3;
    }

    public void setType_3(int type_3) {
        this.type_3 = type_3;
    }

    public int getType_4() {
        return type_4;
    }

    public void setType_4(int type_4) {
        this.type_4 = type_4;
    }
}

class Files{

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