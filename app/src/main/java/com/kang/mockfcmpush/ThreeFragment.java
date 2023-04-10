package com.kang.mockfcmpush;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.File;

public class ThreeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        TextView textView = view.findViewById(R.id.sd_desc);
        textView.setText("1.启动LiveMe->打开彩蛋 ->启动Push收集功能：FCM-Push，放一边后台等待线上下发的PUSH（会记录到push.json文件）\n" +
                "2.获取文件：FCM-Push文件标注《目录位置》下的push.json（注：要有数据）\n" +
                "3.放置文件：" + SecondFragment.getSDPushFile(getActivity()).getPath() + "目录下\n" +
                "4.重启应用");
        return view;
    }
}
