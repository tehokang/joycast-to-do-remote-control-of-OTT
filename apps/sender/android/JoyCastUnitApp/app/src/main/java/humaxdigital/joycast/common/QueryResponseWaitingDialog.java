package humaxdigital.joycast.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import humaxdigital.joycast.unitapp.R;

/**
 * Created by tehokang on 30/03/2017.
 */

public class QueryResponseWaitingDialog extends Dialog {
    public QueryResponseWaitingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.query_response_waiting_dialog);
    }
}
