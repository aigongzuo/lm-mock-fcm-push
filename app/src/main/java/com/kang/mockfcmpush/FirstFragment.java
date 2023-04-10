package com.kang.mockfcmpush;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kang.mockfcmpush.concurrent.LMTask;
import com.kang.mockfcmpush.util.LMThreadUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
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
                    InputStream inputStream = getActivity().getAssets().open("push.json");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        data.add(line);
                    }
                    reader.close();
                    inputStream.close();
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
}
