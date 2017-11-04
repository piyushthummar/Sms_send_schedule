package com.rohan.sms_send_schedule.view;

/**
 * Created by Rohan on 10/24/2017.
 */

import android.content.Context;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Pair;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rohan.sms_send_schedule.R;
import com.rohan.sms_send_schedule.SmsModel;

import java.util.ArrayList;
import java.util.List;

public class BuilderSimForResult extends Builder {

    public static int s_id;
    @Override
    protected RadioGroup getView() {
        return (RadioGroup) view;
    }

    @Override
    public RadioGroup build() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            return getView();
        }
        SubscriptionManager subscriptionManager = (SubscriptionManager) activity2.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        if (null == subscriptionManager) {
            return getView();
        }
        if (subscriptionManager.getActiveSubscriptionInfoCount() < 2) {
            sms.setSubscriptionId(subscriptionManager.getActiveSubscriptionInfoList().get(0).getSubscriptionId());
            return getView();
        }
        getView().setVisibility(View.VISIBLE);
        List<Pair<Integer, String>> simCards = new ArrayList<>();
        for (SubscriptionInfo info : subscriptionManager.getActiveSubscriptionInfoList()) {
            simCards.add(new Pair<>(info.getSubscriptionId(), info.getCarrierName().toString()));
        }
        RadioButton radio1 = getView().findViewById(R.id.radio_sim1_n);
        RadioButton radio2 = getView().findViewById(R.id.radio_sim2_n);
        prepareRadioButton(radio1, simCards.get(0));
        prepareRadioButton(radio2, simCards.get(1));
        if (!radio1.isChecked() && !radio2.isChecked()) {
            radio1.setChecked(true);
        }
        return getView();
    }

    private void prepareRadioButton(RadioButton radioButton, Pair<Integer, String> simCard) {
        radioButton.setTag(simCard.first);
        radioButton.setText(simCard.second);
        radioButton.setOnClickListener(new RadioOnClickListener(sms));
       /* if (simCard.first.equals(sms.getSubscriptionId())) {
            radioButton.setChecked(true);
        }*/
    }

    static class RadioOnClickListener implements View.OnClickListener {

        private SmsModel sms;

        public RadioOnClickListener(SmsModel sms) {
            this.sms = sms;
        }

        @Override
        public void onClick(View v) {
            sms.setSubscriptionId((Integer) v.getTag());
            s_id=sms.getSubscriptionId();

        }
    }
}

