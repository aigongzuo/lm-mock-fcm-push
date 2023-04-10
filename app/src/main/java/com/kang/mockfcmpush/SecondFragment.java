package com.kang.mockfcmpush;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kang.mockfcmpush.concurrent.LMTask;
import com.kang.mockfcmpush.util.LMThreadUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {
    public static final String PUSH_SD_PATH = "lmpush";
    public static final String PUSH_SD_NAME = "push.json";
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        data = new ArrayList<>();
        adapter = new MyAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);
        load();
        return view;
    }

    private void load() {
        LMThreadUtil.runWorkThread(new LMTask() {
            @Override
            public void runInTryCatch() {
                try {
                    List<String> data = new ArrayList<>();
                    File  file = getSDPushFile(getActivity());
                    if (!file.exists() || !file.isFile()) {
                        return;
                    }
                    FileReader fileReader = new FileReader(file);
                    BufferedReader reader = new BufferedReader(fileReader);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        data.add(line);
                    }
                    reader.close();
                    fileReader.close();
                    LMThreadUtil.runUIThread(new LMTask() {
                        @Override
                        public void runInTryCatch() {
                            adapter.setData(data);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static File getSDPushFile(Activity activity) {
        return new File(new File(activity.getExternalFilesDir(null), PUSH_SD_PATH), PUSH_SD_NAME);
    }
}
