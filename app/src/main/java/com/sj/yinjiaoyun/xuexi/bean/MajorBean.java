package com.sj.yinjiaoyun.xuexi.bean;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/19.
 */
public class MajorBean {


    /**
     * state : null
     * success : true
     * message : null
     * data : {"soaProductVOs":[{"id":190,"productName":"im简单的开发6666666666","enrollPlanId":304,"collegeName":"南京大学","productType":0,"productGradation":1,"learningModality":1,"productLogoUrl":"http://139.196.255.175/group1/M00/00/05/i8T_r1fOaNqAaia-AAJaN1FYcd0857.jpg","productLearningLength":4,"enrollPlanSeason":"2017年春季","statusDirection":2,"majorPercent":"0%","soaEnrollPlanDirectionVO":null,"soaEndUserDirectionVO":{"productDirectionId":179,"productDirectionName":"app端im"},"productIntroduction":null,"productTypeText":null,"whetherDirection":null},{"id":202,"productName":"简单的测试777777","enrollPlanId":310,"collegeName":"南京大学","productType":0,"productGradation":1,"learningModality":1,"productLogoUrl":"http://139.196.255.175/group1/M00/00/0C/i8T_r1itQeKAK9vbAAFb-hPub6Q395.jpg","productLearningLength":5,"enrollPlanSeason":"2017年秋季","statusDirection":1,"majorPercent":"","soaEnrollPlanDirectionVO":[{"productDirectionId":182,"productDirectionDesc":"三星Galaxy S8<br />","productDirectionName":"samsung"},{"productDirectionId":183,"productDirectionDesc":"iphone 8<br />","productDirectionName":"apple"},{"productDirectionId":184,"productDirectionDesc":"荣耀 9<br />","productDirectionName":"huawei"}],"soaEndUserDirectionVO":null,"productIntroduction":null,"productTypeText":null,"whetherDirection":null}]}
     */

    private Object state;
    private boolean success;
    private Object message;
    private DataBean data;

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<SoaProductVOsBean> soaProductVOs;

        public List<SoaProductVOsBean> getSoaProductVOs() {
            return soaProductVOs;
        }

        public void setSoaProductVOs(List<SoaProductVOsBean> soaProductVOs) {
            this.soaProductVOs = soaProductVOs;
        }

        public static class SoaProductVOsBean {
            /**
             * id : 190
             * productName : im简单的开发6666666666
             * enrollPlanId : 304
             * collegeName : 南京大学
             * productType : 0
             * productGradation : 1
             * learningModality : 1
             * productLogoUrl : http://139.196.255.175/group1/M00/00/05/i8T_r1fOaNqAaia-AAJaN1FYcd0857.jpg
             * productLearningLength : 4
             * enrollPlanSeason : 2017年春季
             * statusDirection : 2
             * majorPercent : 0%
             * soaEnrollPlanDirectionVO : null
             * soaEndUserDirectionVO : {"productDirectionId":179,"productDirectionName":"app端im"}
             * productIntroduction : null
             * productTypeText : null
             * whetherDirection : null
             */

            private int id;
            private String productName;
            private int enrollPlanId;
            private String collegeName;
            private int productType;
            private int productGradation;
            private int learningModality;
            private String productLogoUrl;
            private int productLearningLength;
            private String enrollPlanSeason;
            private int statusDirection;
            private String majorPercent;
            private List<SoaEnrollPlanDirectionVOBean> soaEnrollPlanDirectionVO;
            private SoaEndUserDirectionVOBean soaEndUserDirectionVO;
            private Object productIntroduction;
            private Object productTypeText;
            private Object whetherDirection;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public int getEnrollPlanId() {
                return enrollPlanId;
            }

            public void setEnrollPlanId(int enrollPlanId) {
                this.enrollPlanId = enrollPlanId;
            }

            public String getCollegeName() {
                return collegeName;
            }

            public void setCollegeName(String collegeName) {
                this.collegeName = collegeName;
            }

            public int getProductType() {
                return productType;
            }

            public void setProductType(int productType) {
                this.productType = productType;
            }

            public int getProductGradation() {
                return productGradation;
            }

            public void setProductGradation(int productGradation) {
                this.productGradation = productGradation;
            }

            public int getLearningModality() {
                return learningModality;
            }

            public void setLearningModality(int learningModality) {
                this.learningModality = learningModality;
            }

            public String getProductLogoUrl() {
                return productLogoUrl;
            }

            public void setProductLogoUrl(String productLogoUrl) {
                this.productLogoUrl = productLogoUrl;
            }

            public int getProductLearningLength() {
                return productLearningLength;
            }

            public void setProductLearningLength(int productLearningLength) {
                this.productLearningLength = productLearningLength;
            }

            public String getEnrollPlanSeason() {
                return enrollPlanSeason;
            }

            public void setEnrollPlanSeason(String enrollPlanSeason) {
                this.enrollPlanSeason = enrollPlanSeason;
            }

            public int getStatusDirection() {
                return statusDirection;
            }

            public void setStatusDirection(int statusDirection) {
                this.statusDirection = statusDirection;
            }

            public String getMajorPercent() {
                return majorPercent;
            }

            public void setMajorPercent(String majorPercent) {
                this.majorPercent = majorPercent;
            }

            public List<SoaEnrollPlanDirectionVOBean> getSoaEnrollPlanDirectionVO() {
                return soaEnrollPlanDirectionVO;
            }

            public void setSoaEnrollPlanDirectionVO(List<SoaEnrollPlanDirectionVOBean> soaEnrollPlanDirectionVO) {
                this.soaEnrollPlanDirectionVO = soaEnrollPlanDirectionVO;
            }

            public SoaEndUserDirectionVOBean getSoaEndUserDirectionVO() {
                return soaEndUserDirectionVO;
            }

            public void setSoaEndUserDirectionVO(SoaEndUserDirectionVOBean soaEndUserDirectionVO) {
                this.soaEndUserDirectionVO = soaEndUserDirectionVO;
            }

            public Object getProductIntroduction() {
                return productIntroduction;
            }

            public void setProductIntroduction(Object productIntroduction) {
                this.productIntroduction = productIntroduction;
            }

            public Object getProductTypeText() {
                return productTypeText;
            }

            public void setProductTypeText(Object productTypeText) {
                this.productTypeText = productTypeText;
            }

            public Object getWhetherDirection() {
                return whetherDirection;
            }

            public void setWhetherDirection(Object whetherDirection) {
                this.whetherDirection = whetherDirection;
            }

            public static class SoaEndUserDirectionVOBean {
                /**
                 * productDirectionId : 179
                 * productDirectionName : app端im
                 */

                private int productDirectionId;
                private String productDirectionName;

                public int getProductDirectionId() {
                    return productDirectionId;
                }

                public void setProductDirectionId(int productDirectionId) {
                    this.productDirectionId = productDirectionId;
                }

                public String getProductDirectionName() {
                    return productDirectionName;
                }

                public void setProductDirectionName(String productDirectionName) {
                    this.productDirectionName = productDirectionName;
                }
            }
            public static class SoaEnrollPlanDirectionVOBean{

                /**
                 * productDirectionId : 184
                 * productDirectionDesc : 荣耀 9<br />
                 * productDirectionName : huawei
                 */

                private int productDirectionId;
                private String productDirectionDesc;
                private String productDirectionName;

                public int getProductDirectionId() {
                    return productDirectionId;
                }

                public void setProductDirectionId(int productDirectionId) {
                    this.productDirectionId = productDirectionId;
                }

                public String getProductDirectionDesc() {
                    return productDirectionDesc;
                }

                public void setProductDirectionDesc(String productDirectionDesc) {
                    this.productDirectionDesc = productDirectionDesc;
                }

                public String getProductDirectionName() {
                    return productDirectionName;
                }

                public void setProductDirectionName(String productDirectionName) {
                    this.productDirectionName = productDirectionName;
                }
            }
        }
    }
}
