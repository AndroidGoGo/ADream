package com.lzq.adream.fragment.mian;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseContactList;
import com.hyphenate.easeui.widget.EaseImageView;
import com.lzq.adream.Constant;
import com.lzq.adream.R;
import com.lzq.adream.activity.ChatActivity;
import com.lzq.adream.activity.MyInfoActivity;
import com.lzq.adream.activity.SearcActivity;
import com.lzq.adream.base.BaseFragment;
import com.lzq.adream.comm.DemoHelper;
import com.lzq.adream.widget.ContactItemView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ${廖昭启} on 2017/11/3.
 */

public class ContactFragment extends BaseFragment implements View.OnClickListener {
    protected EaseContactList contactListLayout;

    private EaseImageView mEaseHead;
    protected ListView listView;
    private RelativeLayout mRlSreach;
    private ContactItemView mApplicationItem;
    private boolean hidden;
    private Map<String, EaseUser> mContactsMap;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_contact;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    protected List<EaseUser> contactList;

    private void setUpView() {

        EMClient.getInstance().addConnectionListener(connectionListener);
        contactList = new ArrayList<>();

        getContactList();
        contactListLayout.init(contactList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseUser user = (EaseUser) listView.getItemAtPosition(position);
              //  hideSoftKeyboard();
                String username = user.getUsername();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                // it's single chat
                intent.putExtra(Constant.EXTRA_USER_ID, username);
                startActivity(intent);
            }
        });
    }

    /**
     *
     */
    @Override
    protected void initView() {
        mEaseHead = (EaseImageView) findViewById(R.id.esIV_head);
        mRlSreach = (RelativeLayout) findViewById(R.id.rl_sreach);
        contactListLayout = (EaseContactList) findViewById(R.id.contact_list);
        listView = contactListLayout.getListView();
        @SuppressLint("InflateParams")View headView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, listView,false);

        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        mApplicationItem = (ContactItemView) headView.findViewById(R.id.application_item);
        mApplicationItem.setOnClickListener(clickListener);
        headView.findViewById(R.id.group_item).setOnClickListener(clickListener);
        headView.findViewById(R.id.chat_room_item).setOnClickListener(clickListener);
        headView.findViewById(R.id.robot_item).setOnClickListener(clickListener);
        headView.findViewById(R.id.conference_item).setOnClickListener(clickListener);
        listView.addHeaderView(headView);
        initListener();
        setUpView();
    }

    private void initListener() {
        mEaseHead.setOnClickListener(this);
        mRlSreach.setOnClickListener(this);
    }

    private void initData() {


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.esIV_head:
                startActivity(MyInfoActivity.class);
                break;
            case R.id.rl_sreach:
                startActivity(SearcActivity.class);
                break;
        }
    }

    private boolean isConflict;
    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED) {
                isConflict = true;
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        onConnectionDisconnected();
                    }

                });
            }
        }

        @Override
        public void onConnected() {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    onConnectionConnected();
                }

            });
        }
    };
    private EaseContactListFragment.EaseContactListItemClickListener listItemClickListener;


    protected void onConnectionDisconnected() {

    }

    protected void onConnectionConnected() {

    }

    /**
     * 获取联系人列表
     */
    protected void getContactList() {
        contactList.clear();
        if (mContactsMap == null) {
            return;
        }
        synchronized (this.mContactsMap) {
            Iterator<Map.Entry<String, EaseUser>> iterator = mContactsMap.entrySet().iterator();
            List<String> blackList = EMClient.getInstance().contactManager().getBlackListUsernames();
            while (iterator.hasNext()) {
                Map.Entry<String, EaseUser> entry = iterator.next();
                // to make it compatible with data in previous version, you can remove this check if this is new app
                if (!entry.getKey().equals("item_new_friends")
                        && !entry.getKey().equals("item_groups")
                        && !entry.getKey().equals("item_chatroom")
                        && !entry.getKey().equals("item_robots")) {
                    if (!blackList.contains(entry.getKey())) {
                        //filter out users in blacklist
                        EaseUser user = entry.getValue();
                        EaseCommonUtils.setUserInitialLetter(user);
                        contactList.add(user);
                    }
                }
            }
        }

        // sorting
        Collections.sort(contactList, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {
                    return lhs.getNick().compareTo(rhs.getNick());
                } else {
                    if ("#".equals(lhs.getInitialLetter())) {
                        return 1;
                    } else if ("#".equals(rhs.getInitialLetter())) {
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }

            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }

    private void refresh() {
        Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            //noinspection unchecked
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>) m).clone();
        }

        setContactsMap(m);
        getContactList();
        contactListLayout.refresh();
    }




    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }


    public void setContactsMap(Map<String,EaseUser> contactsMap) {
        mContactsMap = contactsMap;
    }

    protected class HeaderItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.application_item:
                    // 进入申请与通知页面
                    //   startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                    Toast.makeText(getActivity(),"进入添加新好友界面",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.group_item:
                    // 进入群聊列表页面
                    // startActivity(new Intent(getActivity(), GroupsActivity.class));
                    Toast.makeText(getActivity(),"进入群聊界面",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.chat_room_item:
                    //进入聊天室列表页面
                    //   startActivity(new Intent(getActivity(), PublicChatRoomsActivity.class));
                    Toast.makeText(getActivity(),"进入聊天室界面",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.robot_item:
                    //进入Robot列表页面
                    //  startActivity(new Intent(getActivity(), RobotsActivity.class));
                    Toast.makeText(getActivity(),"进入环信小助手界面",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.conference_item:
                    // startActivity(new Intent(getActivity(), ConferenceActivity.class).putExtra(Constant.EXTRA_CONFERENCE_IS_CREATOR, true));
                    Toast.makeText(getActivity(),"进入音视频会议界面",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    }
}
