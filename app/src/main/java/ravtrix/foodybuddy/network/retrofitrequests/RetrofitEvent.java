package ravtrix.foodybuddy.network.retrofitrequests;

import ravtrix.foodybuddy.network.retrofitinterfaces.RetrofitInterfaceEvents;
import ravtrix.foodybuddy.utils.NetworkUtil;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 3/10/17.
 */

public class RetrofitEvent {

    private Retrofit retrofit;

    public RetrofitEvent() {
        retrofit = NetworkUtil.buildRetrofit();
    }

    public RetrofitInterfaceEvents.GetEvents getEvents() {
        return retrofit.create(RetrofitInterfaceEvents.GetEvents.class);
    }

    public RetrofitInterfaceEvents.GetNearbyEvents getNearbyEvents() {
        return retrofit.create(RetrofitInterfaceEvents.GetNearbyEvents.class);
    }

    public RetrofitInterfaceEvents.GetEventByID getEventByID() {
        return retrofit.create(RetrofitInterfaceEvents.GetEventByID.class);
    }

    public RetrofitInterfaceEvents.PostEvent postEvent() {
        return retrofit.create(RetrofitInterfaceEvents.PostEvent.class);
    }

    public RetrofitInterfaceEvents.JoinEvent joinEvent() {
        return retrofit.create(RetrofitInterfaceEvents.JoinEvent.class);
    }

    public RetrofitInterfaceEvents.GetEventJoined getEventJoined() {
        return retrofit.create(RetrofitInterfaceEvents.GetEventJoined.class);
    }
}
