package com.sj.yinjiaoyun.xuexi.bean;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/6/6.
 * 获取图片验证码的实体类封装
 */
public class ImgCodeBean {


    /**
     * state : 1
     * success : true
     * message :
     * data : {"image":"/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAoAFADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD3i3ukuWnVQyvBIYpFbqDgEdOMFWU/jzg5AqS6zFa3JtrqG4SY7mjEMEkwdAR8wKKfUAg4IPqCCdCRikTusbSMoJCLjLewyQM/UioRfWzWf2vzQIACSxBGMHBBB5BB4x1zx1oAZa6rp19KYrS/tbiQDcVimVyB64B6cim6q3lWXni6jtmhdXDyybEPOCrHphgSuSDgkEDIFYtx4gtILtrmzjmYswE0e0BZgONwyeHA6HuBg9ivG+MNetNa163QPK2n2pKOUC5b5vnZDk5BULjPpXZg8HLEVOXZdTjxWMjQp8y1Z2lpqcX2m8tdNu4ZmW6E8MUbK6yxsA0i7+m8t5zBSwOV/uCuht7iK7toriBt0UqCRGwRlSMg4PtXk+qa/wCEtF01L6ys9WgluHdbcRTEBZYgDlsSZwRLtOCTjdjB5Mmt/GfQ9F0a5j0Sxubi6jQeSZVAiLtjJdt25iCST/eIPzc7q0qZbXV5Qg+XV3a7b/cdNBzqUlUa+7Y9Yqtd2Fte7DPFl0zskVijpnrtdSGXOOcHkcV4H4e07xH490r+2r/4kXlq8kmZbe0DOtrtOEaVI3QRZKMwO3bhdxbJr2bwVNqlx4RsZdavYL3UT5gmngA2MRIwAGAAcDAyBg4yCRyeGUbdSk7l+ODUbaVAt2t5CxG/7SAki+pDIuCAP4So5P3u1El1NYSuboNJaElhcjA8kekg4+UdmAOB97G0sdCipGFc54itJLezZ7d2W3kl3SwgfKGOTuHoCeo6E4PBzu6Oo5oY7iFopUDxsMFTTTs7kyV00ZHhy6iudBKyiNfIeSKZedoGcjOfVWUntz26DitXeKbxxoc8cPktK8RmiDEhJROwcDtwwOSOCcnvk9oPC9n9qaQySeVwViz0OTnJ6kYwPXg8nPGOvhRLjWp5G1Cf7bZzJPbNKqMpRm8zLKoXOZPOGMgjA7Yz2YStClUcpdn+JyYmjOrTUUtmvwOE+MtxK39jWty264t3uVLEAeYh8so+PccEgAblcDgVT+K+iy6Jb6LY7rZrHE3kRQxsgj4jDLhmbg4DZzkszk9a9J8Z+CbPxR/Zc1+05kt3EUz2ahWdXwMgENwGweTwpfqateM/Ael+OYbWLU7i8hW237PszqpO7bnO5T/dH616eFzSlRhh07vk57rzd7fn8j1I1VFR8rnlPiP4a6j4LuNR8QeDtWuLKayDXBtS+PMtgd7FWJw6p8qmN852ZJ+ZVPa/B/xVqnifRLm41KJCfPZRLChVS6pHvLDopbeGwOreYeOBXLz/AAq1u/KWmt+OdVumJAaxmY/6TGGVnETtKyn7mRuHBVSwUYNeqeFPClh4P0n+zNNkma2zuxKE3Fj1YlVBYnjrngADAGK8SUk1bdnMlqbtVLqe9glBhsluIAPm2TBZc+gVgFI6clh3/FukXEtzpNtJO265CeXOcAfvV+VxxxwwYccenFXayKOek0u8t5Xaxs1tokJ2C01Blwv+zCyeVuIzweMnOQfmq7pUuoGWSK8guhEBmOW4WEN7hjG5BPPGFXAHOTRRQBZlung1K3hcL5FwGVG7iQAttPqCoY9sbDydwxZEaCVpQiiRgFZsckDOAT6DJ/M0UUARS3SQXFvC4YGcsqN23AFtp75IDH0+U85xmeiigCO4t4rqBoZl3I2O5BBByCCOQQcEEcgjIqC1guraUxNMs1qB+7L581PQE87x154IwM7iSaKKAJ4LeK2jMcK7VLtIRkn5mYsx59SSai1COebT54rbiWRCinzTGRnjIYK2CByODzRRQB//2Q==","uuid":"bb30907c-cf6e-407b-82a9-9417ab3af337"}
     */

    private int state;
    private boolean success;
    private String message;
    private DataBean data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * image : 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
         * uuid : bb30907c-cf6e-407b-82a9-9417ab3af337
         */

        private String image;
        private String uuid;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }
}
