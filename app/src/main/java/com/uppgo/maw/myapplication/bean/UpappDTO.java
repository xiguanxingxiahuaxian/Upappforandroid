package com.uppgo.maw.myapplication.bean;

import java.util.List;

/**
 * 项目名称：Upapp
 * 类描述：
 * 创建人：maw@neuqsoft.com
 * 创建时间： 2017/9/8 10:18
 * 修改备注
 */
public class UpappDTO {

    /**
     * vercode : 1
     * vername :
     * url : http://10.19.92.137:8080/android.apk
     * content : ["1","2","3"]
     */

    private int vercode;
    private String vername;
    private String url;
    private List<String> content;

    public int getVercode() {
        return vercode;
    }

    public void setVercode(int vercode) {
        this.vercode = vercode;
    }

    public String getVername() {
        return vername;
    }

    public void setVername(String vername) {
        this.vername = vername;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
