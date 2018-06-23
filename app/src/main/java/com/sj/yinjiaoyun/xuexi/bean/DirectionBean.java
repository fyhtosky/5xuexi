package com.sj.yinjiaoyun.xuexi.bean;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/20.
 * 选择专业方向的实体类
 */
public class DirectionBean {

    /**
     * state : 1
     * success : true
     * message :
     * data : {"enrollPlanDirections":[{"productDirectionId":178,"productDirectionDesc":"<p>\n\tweb端im\n<\/p>\n<p>\n\t6666666\n<\/p>","productDirectionName":"web端im"},{"productDirectionId":179,"productDirectionDesc":"<p>\n\tapp端im\n<\/p>\n<p>\n\t6666666\n<\/p>","productDirectionName":"app端im"}]}
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
        private List<EnrollPlanDirectionsBean> enrollPlanDirections;

        public List<EnrollPlanDirectionsBean> getEnrollPlanDirections() {
            return enrollPlanDirections;
        }

        public void setEnrollPlanDirections(List<EnrollPlanDirectionsBean> enrollPlanDirections) {
            this.enrollPlanDirections = enrollPlanDirections;
        }

        public static class EnrollPlanDirectionsBean {
            /**
             * productDirectionId : 178
             * productDirectionDesc : <p>
             web端im
             </p>
             <p>
             6666666
             </p>
             * productDirectionName : web端im
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
