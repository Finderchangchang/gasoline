package com.app.bulkgasoline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.bulkgasoline.model.PoliceModel;
import com.app.bulkgasoline.task.GetKeyAlarmTask;
import com.app.bulkgasoline.task.GetKeyAlarmTask.GetKeyAlarmListener;
import com.app.bulkgasoline.utils.Utils;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 一键报警页面。
 */
public class FragmentKeyAlarm extends BaseFragment {

    private ListView listView = null;
    private ArrayList<PoliceModel> mPolices;
    private LinearLayout police_no_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected int getLayoutResourceId() {
        return R.layout.layout_keyalarm;
    }

    protected void initFragment(Bundle savedInstanceState) {
        listView = (ListView) mContentView.findViewById(R.id.police_list);
        police_no_data = (LinearLayout) mContentView.findViewById(R.id.police_no_data);
        // 获得辖区编码
        String departmentId = Utils.ReadString(mContext, Utils.KEY_USERNAME);
        GetKeyAlarmTask getKey = new GetKeyAlarmTask(getGlobalModel(),
                getAuthorizeModel(), departmentId);
        getKey.start(getKeyAlarmListener);
    }

    private GetKeyAlarmListener getKeyAlarmListener = new GetKeyAlarmListener() {
        @Override
        public void OnResult(boolean result,
                             ArrayList<PoliceModel> mPoliceList, String message) {
            List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
            for (int j = 0; j < mPoliceList.size(); j++) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("PoliceName", mPoliceList.get(j).PoliceName);
                listItem.put("PoliceLinkway", mPoliceList.get(j).PoliceLinkway);
                listItems.add(listItem);
            }
            if (mPoliceList.size() > 0) {
                listView.setVisibility(View.VISIBLE);
                police_no_data.setVisibility(View.GONE);
                KeyAlermAdspter alerm = new KeyAlermAdspter(mContext, mPoliceList);
                listView.setAdapter(alerm);
                listView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        System.out.println("6666666666");

                    }
                });
            } else {
                listView.setVisibility(View.GONE);
                police_no_data.setVisibility(View.VISIBLE);
            }
        }
    };
}