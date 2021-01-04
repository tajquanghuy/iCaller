package com.example.icaller_mobile.model.network.room;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ContactReportResponse implements Parcelable {

    /**
     * data : [{"id":5000010,"code":"+84","phone":"+841068","name":"T.t Kinh Te - Xh","warn_type":1,"updated_at":"2020-04-24 10:12:35","user":null},{"id":5000009,"code":"+84","phone":"+84900","name":"T","warn_type":1,"updated_at":"2020-04-24 04:50:43","user":null},{"id":5000008,"code":"+84","phone":"+8418008098","name":"Cskh","warn_type":1,"updated_at":"2020-04-24 02:42:25","user":null},{"id":5000007,"code":"+84","phone":"+84*098#","name":"Khuyenmai","warn_type":1,"updated_at":"2020-04-24 02:41:39","user":null},{"id":5000006,"code":"+84","phone":"+84939142477","name":"Le Thuy Linh","warn_type":1,"updated_at":"2020-04-24 02:40:40","user":null},{"id":5000005,"code":"+84","phone":"+848345","name":"T4","warn_type":1,"updated_at":"2020-04-24 02:16:37","user":null},{"id":5000004,"code":"+84","phone":"+84337657452","name":"B","warn_type":1,"updated_at":"2020-04-24 02:15:43","user":null},{"id":5000003,"code":"+84","phone":"+84785169744","name":"Nga","warn_type":1,"updated_at":"2020-04-24 02:14:49","user":null},{"id":5000002,"code":"+84","phone":"+84965999999","name":null,"warn_type":1,"updated_at":"2020-04-23 18:08:40","user":null},{"id":5000001,"code":"+84","phone":"+84965999999","name":null,"warn_type":1,"updated_at":"2020-04-23 18:08:17","user":null},{"id":5000000,"code":"+84","phone":"+84972201739","name":"name 10000","warn_type":1,"updated_at":null,"user":null},{"id":4999999,"code":"+84","phone":"+84995846413","name":"name 9999","warn_type":1,"updated_at":null,"user":null},{"id":4999998,"code":"+84","phone":"+84918643598","name":"name 9998","warn_type":1,"updated_at":null,"user":null},{"id":4999997,"code":"+84","phone":"+84930987971","name":"name 9997","warn_type":1,"updated_at":null,"user":null},{"id":4999996,"code":"+84","phone":"+84981489643","name":"name 9996","warn_type":1,"updated_at":null,"user":null},{"id":4999995,"code":"+84","phone":"+84996749604","name":"name 9995","warn_type":1,"updated_at":null,"user":null},{"id":4999994,"code":"+84","phone":"+84961297697","name":"name 9994","warn_type":1,"updated_at":null,"user":null},{"id":4999993,"code":"+84","phone":"+84987908693","name":"name 9993","warn_type":1,"updated_at":null,"user":null},{"id":4999992,"code":"+84","phone":"+84914746192","name":"name 9992","warn_type":1,"updated_at":null,"user":null},{"id":4999991,"code":"+84","phone":"+84946090909","name":"name 9991","warn_type":1,"updated_at":null,"user":null},{"id":4999990,"code":"+84","phone":"+84963563219","name":"name 9990","warn_type":1,"updated_at":null,"user":null},{"id":4999989,"code":"+84","phone":"+84912161829","name":"name 9989","warn_type":1,"updated_at":null,"user":null},{"id":4999988,"code":"+84","phone":"+84963131112","name":"name 9988","warn_type":1,"updated_at":null,"user":null},{"id":4999987,"code":"+84","phone":"+84939239677","name":"name 9987","warn_type":1,"updated_at":null,"user":null},{"id":4999986,"code":"+84","phone":"+84976738338","name":"name 9986","warn_type":1,"updated_at":null,"user":null},{"id":4999985,"code":"+84","phone":"+84995021977","name":"name 9985","warn_type":1,"updated_at":null,"user":null},{"id":4999984,"code":"+84","phone":"+84992410759","name":"name 9984","warn_type":1,"updated_at":null,"user":null},{"id":4999983,"code":"+84","phone":"+84953467376","name":"name 9983","warn_type":1,"updated_at":null,"user":null},{"id":4999982,"code":"+84","phone":"+84977381940","name":"name 9982","warn_type":1,"updated_at":null,"user":null},{"id":4999981,"code":"+84","phone":"+84920161696","name":"name 9981","warn_type":1,"updated_at":null,"user":null},{"id":4999980,"code":"+84","phone":"+84914429278","name":"name 9980","warn_type":1,"updated_at":null,"user":null},{"id":4999979,"code":"+84","phone":"+84997376685","name":"name 9979","warn_type":1,"updated_at":null,"user":null},{"id":4999978,"code":"+84","phone":"+84912197854","name":"name 9978","warn_type":1,"updated_at":null,"user":null},{"id":4999977,"code":"+84","phone":"+84964912434","name":"name 9977","warn_type":1,"updated_at":null,"user":null},{"id":4999976,"code":"+84","phone":"+84988049951","name":"name 9976","warn_type":1,"updated_at":null,"user":null},{"id":4999975,"code":"+84","phone":"+84920831940","name":"name 9975","warn_type":1,"updated_at":null,"user":null},{"id":4999974,"code":"+84","phone":"+84999794506","name":"name 9974","warn_type":1,"updated_at":null,"user":null},{"id":4999973,"code":"+84","phone":"+84983675987","name":"name 9973","warn_type":1,"updated_at":null,"user":null},{"id":4999972,"code":"+84","phone":"+84992533767","name":"name 9972","warn_type":1,"updated_at":null,"user":null},{"id":4999971,"code":"+84","phone":"+84999294057","name":"name 9971","warn_type":1,"updated_at":null,"user":null},{"id":4999970,"code":"+84","phone":"+84987156154","name":"name 9970","warn_type":1,"updated_at":null,"user":null},{"id":4999969,"code":"+84","phone":"+84983514964","name":"name 9969","warn_type":1,"updated_at":null,"user":null},{"id":4999968,"code":"+84","phone":"+84938703853","name":"name 9968","warn_type":1,"updated_at":null,"user":null},{"id":4999967,"code":"+84","phone":"+84939980181","name":"name 9967","warn_type":1,"updated_at":null,"user":null},{"id":4999966,"code":"+84","phone":"+84959435553","name":"name 9966","warn_type":1,"updated_at":null,"user":null},{"id":4999965,"code":"+84","phone":"+84939940445","name":"name 9965","warn_type":1,"updated_at":null,"user":null},{"id":4999964,"code":"+84","phone":"+84916445793","name":"name 9964","warn_type":1,"updated_at":null,"user":null},{"id":4999963,"code":"+84","phone":"+84938826750","name":"name 9963","warn_type":1,"updated_at":null,"user":null},{"id":4999962,"code":"+84","phone":"+84995679832","name":"name 9962","warn_type":1,"updated_at":null,"user":null},{"id":4999961,"code":"+84","phone":"+84999331236","name":"name 9961","warn_type":1,"updated_at":null,"user":null},{"id":4999960,"code":"+84","phone":"+84974057185","name":"name 9960","warn_type":1,"updated_at":null,"user":null},{"id":4999959,"code":"+84","phone":"+84916886020","name":"name 9959","warn_type":1,"updated_at":null,"user":null},{"id":4999958,"code":"+84","phone":"+84991070304","name":"name 9958","warn_type":1,"updated_at":null,"user":null},{"id":4999957,"code":"+84","phone":"+84941206699","name":"name 9957","warn_type":1,"updated_at":null,"user":null},{"id":4999956,"code":"+84","phone":"+84988360407","name":"name 9956","warn_type":1,"updated_at":null,"user":null},{"id":4999955,"code":"+84","phone":"+84951419069","name":"name 9955","warn_type":1,"updated_at":null,"user":null},{"id":4999954,"code":"+84","phone":"+84981471066","name":"name 9954","warn_type":1,"updated_at":null,"user":null},{"id":4999953,"code":"+84","phone":"+84984879895","name":"name 9953","warn_type":1,"updated_at":null,"user":null},{"id":4999952,"code":"+84","phone":"+84991158492","name":"name 9952","warn_type":1,"updated_at":null,"user":null},{"id":4999951,"code":"+84","phone":"+84942348341","name":"name 9951","warn_type":1,"updated_at":null,"user":null},{"id":4999950,"code":"+84","phone":"+84994360799","name":"name 9950","warn_type":1,"updated_at":null,"user":null},{"id":4999949,"code":"+84","phone":"+84917709245","name":"name 9949","warn_type":1,"updated_at":null,"user":null},{"id":4999948,"code":"+84","phone":"+84935536325","name":"name 9948","warn_type":1,"updated_at":null,"user":null},{"id":4999947,"code":"+84","phone":"+84959883022","name":"name 9947","warn_type":1,"updated_at":null,"user":null},{"id":4999946,"code":"+84","phone":"+84920151916","name":"name 9946","warn_type":1,"updated_at":null,"user":null},{"id":4999945,"code":"+84","phone":"+84913628693","name":"name 9945","warn_type":1,"updated_at":null,"user":null},{"id":4999944,"code":"+84","phone":"+84939959358","name":"name 9944","warn_type":1,"updated_at":null,"user":null},{"id":4999943,"code":"+84","phone":"+84921849902","name":"name 9943","warn_type":1,"updated_at":null,"user":null},{"id":4999942,"code":"+84","phone":"+84922449699","name":"name 9942","warn_type":1,"updated_at":null,"user":null},{"id":4999941,"code":"+84","phone":"+84911844534","name":"name 9941","warn_type":1,"updated_at":null,"user":null},{"id":4999940,"code":"+84","phone":"+84941455933","name":"name 9940","warn_type":1,"updated_at":null,"user":null},{"id":4999939,"code":"+84","phone":"+84913590674","name":"name 9939","warn_type":1,"updated_at":null,"user":null},{"id":4999938,"code":"+84","phone":"+84997883379","name":"name 9938","warn_type":1,"updated_at":null,"user":null},{"id":4999937,"code":"+84","phone":"+84954449622","name":"name 9937","warn_type":1,"updated_at":null,"user":null},{"id":4999936,"code":"+84","phone":"+84945536058","name":"name 9936","warn_type":1,"updated_at":null,"user":null},{"id":4999935,"code":"+84","phone":"+84968436757","name":"name 9935","warn_type":1,"updated_at":null,"user":null},{"id":4999934,"code":"+84","phone":"+84930433167","name":"name 9934","warn_type":1,"updated_at":null,"user":null},{"id":4999933,"code":"+84","phone":"+84985077730","name":"name 9933","warn_type":1,"updated_at":null,"user":null},{"id":4999932,"code":"+84","phone":"+84918912346","name":"name 9932","warn_type":1,"updated_at":null,"user":null},{"id":4999931,"code":"+84","phone":"+84961596089","name":"name 9931","warn_type":1,"updated_at":null,"user":null},{"id":4999930,"code":"+84","phone":"+84926779905","name":"name 9930","warn_type":1,"updated_at":null,"user":null},{"id":4999929,"code":"+84","phone":"+84922791155","name":"name 9929","warn_type":1,"updated_at":null,"user":null},{"id":4999928,"code":"+84","phone":"+84917983840","name":"name 9928","warn_type":1,"updated_at":null,"user":null},{"id":4999927,"code":"+84","phone":"+84960604354","name":"name 9927","warn_type":1,"updated_at":null,"user":null},{"id":4999926,"code":"+84","phone":"+84913465670","name":"name 9926","warn_type":1,"updated_at":null,"user":null},{"id":4999925,"code":"+84","phone":"+84987170695","name":"name 9925","warn_type":1,"updated_at":null,"user":null},{"id":4999924,"code":"+84","phone":"+84987356096","name":"name 9924","warn_type":1,"updated_at":null,"user":null},{"id":4999923,"code":"+84","phone":"+84968608258","name":"name 9923","warn_type":1,"updated_at":null,"user":null},{"id":4999922,"code":"+84","phone":"+84993948911","name":"name 9922","warn_type":1,"updated_at":null,"user":null},{"id":4999921,"code":"+84","phone":"+84971532779","name":"name 9921","warn_type":1,"updated_at":null,"user":null},{"id":4999920,"code":"+84","phone":"+84964792914","name":"name 9920","warn_type":1,"updated_at":null,"user":null},{"id":4999919,"code":"+84","phone":"+84914631453","name":"name 9919","warn_type":1,"updated_at":null,"user":null},{"id":4999918,"code":"+84","phone":"+84914106779","name":"name 9918","warn_type":1,"updated_at":null,"user":null},{"id":4999917,"code":"+84","phone":"+84938898784","name":"name 9917","warn_type":1,"updated_at":null,"user":null},{"id":4999916,"code":"+84","phone":"+84953435093","name":"name 9916","warn_type":1,"updated_at":null,"user":null},{"id":4999915,"code":"+84","phone":"+84962458415","name":"name 9915","warn_type":1,"updated_at":null,"user":null},{"id":4999914,"code":"+84","phone":"+84917227949","name":"name 9914","warn_type":1,"updated_at":null,"user":null},{"id":4999913,"code":"+84","phone":"+84971500219","name":"name 9913","warn_type":1,"updated_at":null,"user":null},{"id":4999912,"code":"+84","phone":"+84930718079","name":"name 9912","warn_type":1,"updated_at":null,"user":null},{"id":4999911,"code":"+84","phone":"+84973355669","name":"name 9911","warn_type":1,"updated_at":null,"user":null}]
     * timestamp : 2020-04-28 03:31:03
     * phone_deleted : 184,56,147,55,219,36756,36754,36757,36769,36736,36764,36771,36772,36731,36813
     */

    private String timestamp;
    private String phone_deleted;
    private List<DataBean> data;

    public ContactReportResponse() {
    }

    public ContactReportResponse(String timestamp, String phone_deleted, List<DataBean> data) {
        this.timestamp = timestamp;
        this.phone_deleted = phone_deleted;
        this.data = data;
    }

    protected ContactReportResponse(Parcel in) {
        timestamp = in.readString();
        phone_deleted = in.readString();
        data = in.createTypedArrayList(DataBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timestamp);
        dest.writeString(phone_deleted);
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactReportResponse> CREATOR = new Creator<ContactReportResponse>() {
        @Override
        public ContactReportResponse createFromParcel(Parcel in) {
            return new ContactReportResponse(in);
        }

        @Override
        public ContactReportResponse[] newArray(int size) {
            return new ContactReportResponse[size];
        }
    };

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhone_deleted() {
        return phone_deleted;
    }

    public void setPhone_deleted(String phone_deleted) {
        this.phone_deleted = phone_deleted;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }
}
