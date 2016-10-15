package az.colorweather.api.listener;

import az.colorweather.api.model.OWResponse;

/**
 * Created by az on 15/10/16.
 */

public interface OWRequestListener<T> {
    void onResponse(OWResponse<T> response);

    void onFailure(Throwable t);
}
