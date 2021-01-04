package com.example.icaller_mobile.model.network.response.contact_respone;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.icaller_mobile.model.network.room.DataBean;

import java.util.List;

public class DataBeanX implements Parcelable {
    /**
     * current_page : 1
     * data : [{"id":93731,"code":"+84","phone":"+84982171696","name":"Hồng Nhạn","warn_type":1,"user":null},{"id":93730,"code":"+84","phone":"+845113622696","name":"Hồng Nhạn","warn_type":1,"user":null},{"id":93729,"code":"+84","phone":"+845113244539","name":"Hồng Nhạn","warn_type":1,"user":null},{"id":93728,"code":"+84","phone":"+8489060111","name":"Cảnh Linh","warn_type":1,"user":null},{"id":93727,"code":"+84","phone":"+84903685458","name":"Minh Thông","warn_type":1,"user":null},{"id":93723,"code":"+84","phone":"+8478070972","name":"Quốc Khánh","warn_type":1,"user":null},{"id":93722,"code":"+84","phone":"+848218189914","name":"Quốc Khánh","warn_type":1,"user":null},{"id":93720,"code":"+84","phone":"+84989008185","name":"Hoài Nam","warn_type":1,"user":null},{"id":93719,"code":"+84","phone":"+84613935897","name":"Hoài Nam","warn_type":1,"user":null},{"id":93718,"code":"+84","phone":"+84613935896","name":"Hoài Nam","warn_type":1,"user":null},{"id":93717,"code":"+84","phone":"+84903731891","name":"Văn Ánh","warn_type":1,"user":null},{"id":93716,"code":"+84","phone":"+84913731891","name":"Văn Ánh","warn_type":1,"user":null},{"id":93715,"code":"+84","phone":"+84913175499","name":"Xuân Huy","warn_type":1,"user":null},{"id":93714,"code":"+84","phone":"+84919695590","name":"Xuân Huy","warn_type":1,"user":null},{"id":93713,"code":"+84","phone":"+84903038843","name":"Tuyết Trang","warn_type":1,"user":null},{"id":93712,"code":"+84","phone":"+84938237237","name":"Tuyết Trang","warn_type":1,"user":null},{"id":93710,"code":"+84","phone":"+84983677622","name":"Thanh Việt","warn_type":1,"user":null},{"id":93708,"code":"+84","phone":"+8463835750","name":"Hữu Dũng","warn_type":1,"user":null},{"id":93707,"code":"+84","phone":"+84913920079","name":"Nguyễn Nga","warn_type":1,"user":null},{"id":93706,"code":"+84","phone":"+84650553510","name":"Nguyễn Nga","warn_type":1,"user":null},{"id":93705,"code":"+84","phone":"+84650782700","name":"Đỗ Định","warn_type":1,"user":null},{"id":93704,"code":"+84","phone":"+84650782770","name":"Đỗ Định","warn_type":1,"user":null},{"id":93703,"code":"+84","phone":"+84913995774","name":"Kim Thoa","warn_type":1,"user":null},{"id":93701,"code":"+84","phone":"+84650756606","name":"Bình Tiến","warn_type":1,"user":null},{"id":93700,"code":"+84","phone":"+84650756605","name":"Bình Tiến","warn_type":1,"user":null},{"id":93699,"code":"+84","phone":"+84937000911","name":"Tuấn Anh","warn_type":1,"user":null},{"id":93698,"code":"+84","phone":"+84650740713","name":"Minh Triết","warn_type":1,"user":null},{"id":93697,"code":"+84","phone":"+84650710070","name":"Minh Triết","warn_type":1,"user":null},{"id":93696,"code":"+84","phone":"+84903902665","name":"Tuấn Minh","warn_type":1,"user":null},{"id":93695,"code":"+84","phone":"+8464833076","name":"Tuấn Minh","warn_type":1,"user":null},{"id":93694,"code":"+84","phone":"+8464833527","name":"Tuấn Minh","warn_type":1,"user":null},{"id":93693,"code":"+84","phone":"+84613836410","name":"Đại Đức","warn_type":1,"user":null},{"id":93692,"code":"+84","phone":"+84613836515","name":"Đại Đức","warn_type":1,"user":null},{"id":93690,"code":"+84","phone":"+84989000336","name":"Quốc Phong","warn_type":1,"user":null},{"id":93689,"code":"+84","phone":"+84908105111","name":"Quốc Phong","warn_type":1,"user":null},{"id":93686,"code":"+84","phone":"+84913951250","name":"Tiểu Mai","warn_type":1,"user":null},{"id":93685,"code":"+84","phone":"+84650754208","name":"Tiểu Mai","warn_type":1,"user":null},{"id":93684,"code":"+84","phone":"+84650755308","name":"Tiểu Mai","warn_type":1,"user":null},{"id":93683,"code":"+84","phone":"+84903922630","name":"Văn Cường","warn_type":1,"user":null},{"id":93682,"code":"+84","phone":"+84650743099","name":"Văn Cường","warn_type":1,"user":null},{"id":93678,"code":"+84","phone":"+84988658907","name":"Vĩnh Thọ","warn_type":1,"user":null},{"id":93677,"code":"+84","phone":"+84903804608","name":"Thu Dung","warn_type":1,"user":null},{"id":93676,"code":"+84","phone":"+84983804618","name":"Thu Dung","warn_type":1,"user":null},{"id":93675,"code":"+84","phone":"+8473826096","name":"Nguyễn Một","warn_type":1,"user":null},{"id":93674,"code":"+84","phone":"+8473826526","name":"Nguyễn Một","warn_type":1,"user":null},{"id":93673,"code":"+84","phone":"+8482080945","name":"Ngọc Linh","warn_type":1,"user":null},{"id":93672,"code":"+84","phone":"+84903502204","name":"Thành Hiệp","warn_type":1,"user":null},{"id":93671,"code":"+84","phone":"+84650743754","name":"Thành Hiệp","warn_type":1,"user":null},{"id":93670,"code":"+84","phone":"+84650743750","name":"Thành Hiệp","warn_type":1,"user":null},{"id":93669,"code":"+84","phone":"+84650758340","name":"Hoàng Mai","warn_type":1,"user":null},{"id":93668,"code":"+84","phone":"+84650758341","name":"Hoàng Mai","warn_type":1,"user":null},{"id":93667,"code":"+84","phone":"+8462874463","name":"Sơn Khải","warn_type":1,"user":null},{"id":93666,"code":"+84","phone":"+8462874458","name":"Sơn Khải","warn_type":1,"user":null},{"id":93662,"code":"+84","phone":"+84903666572","name":"Xuân Vũ","warn_type":1,"user":null},{"id":93661,"code":"+84","phone":"+84983567693","name":"Xuân Vũ","warn_type":1,"user":null},{"id":93660,"code":"+84","phone":"+8470957297","name":"Phúc Thiên","warn_type":1,"user":null},{"id":93659,"code":"+84","phone":"+8470957172","name":"Phúc Thiên","warn_type":1,"user":null},{"id":93658,"code":"+84","phone":"+84650155056","name":"Phạm Nụ","warn_type":1,"user":null},{"id":93657,"code":"+84","phone":"+84650755055","name":"Phạm Nụ","warn_type":1,"user":null},{"id":93656,"code":"+84","phone":"+84917707990","name":"Minh Nhật","warn_type":1,"user":null},{"id":93655,"code":"+84","phone":"+84919337979","name":"Hồng Kỳ","warn_type":1,"user":null},{"id":93654,"code":"+84","phone":"+84989337979","name":"Hồng Kỳ","warn_type":1,"user":null},{"id":93653,"code":"+84","phone":"+8464962880","name":"Hồng Kỳ","warn_type":1,"user":null},{"id":93652,"code":"+84","phone":"+8464962878","name":"Hồng Kỳ","warn_type":1,"user":null},{"id":93651,"code":"+84","phone":"+84907888887","name":"Quốc Khang","warn_type":1,"user":null},{"id":93650,"code":"+84","phone":"+84958806660","name":"Quốc Khang","warn_type":1,"user":null},{"id":93649,"code":"+84","phone":"+84983965296","name":"Trọng Huy","warn_type":1,"user":null},{"id":93648,"code":"+84","phone":"+8475820685","name":"Trọng Huy","warn_type":1,"user":null},{"id":93647,"code":"+84","phone":"+8475813096","name":"Trọng Huy","warn_type":1,"user":null},{"id":93643,"code":"+84","phone":"+84903903201","name":"Thành Nguyên","warn_type":1,"user":null},{"id":93638,"code":"+84","phone":"+84613934458","name":"Duy Vũ","warn_type":1,"user":null},{"id":93637,"code":"+84","phone":"+84613934457","name":"Duy Vũ","warn_type":1,"user":null},{"id":93636,"code":"+84","phone":"+84613934456","name":"Duy Vũ","warn_type":1,"user":null},{"id":93629,"code":"+84","phone":"+84913950642","name":"Tấn Tâm","warn_type":1,"user":null},{"id":93628,"code":"+84","phone":"+84650780445","name":"Tấn Tâm","warn_type":1,"user":null},{"id":93627,"code":"+84","phone":"+84650780444","name":"Tấn Tâm","warn_type":1,"user":null},{"id":93626,"code":"+84","phone":"+84908303985","name":"Trọng Sự","warn_type":1,"user":null},{"id":93625,"code":"+84","phone":"+8476883296","name":"Trọng Sự","warn_type":1,"user":null},{"id":93624,"code":"+84","phone":"+8476883295","name":"Trọng Sự","warn_type":1,"user":null},{"id":93623,"code":"+84","phone":"+84909111776","name":"Huy Sáng","warn_type":1,"user":null},{"id":93621,"code":"+84","phone":"+8489967155","name":"Văn Phúc","warn_type":1,"user":null},{"id":93620,"code":"+84","phone":"+84613952722","name":"Bích Nhuần","warn_type":1,"user":null},{"id":93619,"code":"+84","phone":"+84613952089","name":"Bích Nhuần","warn_type":1,"user":null},{"id":93618,"code":"+84","phone":"+84903413636","name":"Trọng Ngôn","warn_type":1,"user":null},{"id":93617,"code":"+84","phone":"+8461512005","name":"Trọng Ngôn","warn_type":1,"user":null},{"id":93616,"code":"+84","phone":"+8488486059","name":"Trọng Ngôn","warn_type":1,"user":null},{"id":93615,"code":"+84","phone":"+8461511179","name":"Trọng Ngôn","warn_type":1,"user":null},{"id":93614,"code":"+84","phone":"+84955313535","name":"Ngọc Minh","warn_type":1,"user":null},{"id":93613,"code":"+84","phone":"+84903313535","name":"Ngọc Minh","warn_type":1,"user":null},{"id":93611,"code":"+84","phone":"+84909111775","name":"Ngọc Minh","warn_type":1,"user":null},{"id":93606,"code":"+84","phone":"+84902759998","name":"Ái Lan","warn_type":1,"user":null},{"id":93605,"code":"+84","phone":"+8463711622","name":"Ái Lan","warn_type":1,"user":null},{"id":93604,"code":"+84","phone":"+8463212999","name":"Ái Lan","warn_type":1,"user":null},{"id":93603,"code":"+84","phone":"+8463212998","name":"Ái Lan","warn_type":1,"user":null},{"id":93602,"code":"+84","phone":"+84983667167","name":"Ngọc Lâm","warn_type":1,"user":null},{"id":93601,"code":"+84","phone":"+8475860201","name":"Ngọc Lâm","warn_type":1,"user":null},{"id":93600,"code":"+84","phone":"+8475869813","name":"Ngọc Lâm","warn_type":1,"user":null},{"id":93599,"code":"+84","phone":"+849950110918","name":"Trần Khoa","warn_type":1,"user":null},{"id":93598,"code":"+84","phone":"+84824284052","name":"Trần Khoa","warn_type":1,"user":null},{"id":93597,"code":"+84","phone":"+84913909426","name":"Hành Trưởng","warn_type":1,"user":null}]
     * first_page_url : http://icaller.grooo.com.vn/v1/phone/get-db?page=1
     * from : 1
     * last_page : 708
     * last_page_url : http://icaller.grooo.com.vn/v1/phone/get-db?page=708
     * next_page_url : http://icaller.grooo.com.vn/v1/phone/get-db?page=2
     * path : http://icaller.grooo.com.vn/v1/phone/get-db
     * per_page : 100
     * prev_page_url : null
     * to : 100
     * total : 70750
     */

    private int current_page;
    private String first_page_url;
    private int from;
    private int last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private String per_page;
    private Object prev_page_url;
    private int to;
    private int total;
    private List<DataBean> data;

    public DataBeanX() {
    }

    public DataBeanX(int current_page, String first_page_url, int from, int last_page, String last_page_url, String next_page_url, String path, String per_page, Object prev_page_url, int to, int total, List<DataBean> data) {
        this.current_page = current_page;
        this.first_page_url = first_page_url;
        this.from = from;
        this.last_page = last_page;
        this.last_page_url = last_page_url;
        this.next_page_url = next_page_url;
        this.path = path;
        this.per_page = per_page;
        this.prev_page_url = prev_page_url;
        this.to = to;
        this.total = total;
        this.data = data;
    }

    protected DataBeanX(Parcel in) {
        current_page = in.readInt();
        first_page_url = in.readString();
        from = in.readInt();
        last_page = in.readInt();
        last_page_url = in.readString();
        next_page_url = in.readString();
        path = in.readString();
        per_page = in.readString();
        to = in.readInt();
        total = in.readInt();
        data = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Creator<DataBeanX> CREATOR = new Creator<DataBeanX>() {
        @Override
        public DataBeanX createFromParcel(Parcel in) {
            return new DataBeanX(in);
        }

        @Override
        public DataBeanX[] newArray(int size) {
            return new DataBeanX[size];
        }
    };

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(current_page);
        dest.writeString(first_page_url);
        dest.writeInt(from);
        dest.writeInt(last_page);
        dest.writeString(last_page_url);
        dest.writeString(next_page_url);
        dest.writeString(path);
        dest.writeString(per_page);
        dest.writeInt(to);
        dest.writeInt(total);
        dest.writeTypedList(data);
    }
}
