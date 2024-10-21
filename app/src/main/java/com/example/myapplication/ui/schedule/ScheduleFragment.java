package com.example.myapplication.ui.schedule;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.AddActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentScheduleBinding;
import com.example.myapplication.databinding.ItemSchduleBinding;
import com.example.myapplication.ui.map.MapFragment;
import com.example.myapplication.util.BindAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScheduleFragment extends Fragment {

    private ScheduleViewModel mViewModel;

    public ScheduleFragment() {
        super(R.layout.fragment_schedule);
    }

    private BindAdapter<ItemSchduleBinding, String> adapter = new BindAdapter<ItemSchduleBinding, String>() {
        @Override
        public ItemSchduleBinding createHolder(ViewGroup parent) {
            return ItemSchduleBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemSchduleBinding item, String data, int position) {
            item.tvNumber.setText(String.valueOf(position + 1));
            item.tvContent.setText(data);
            item.cardView.setCardBackgroundColor(randomColor());
            item.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction =
                            requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_content_main, new MapFragment());
                    transaction.addToBackStack(null);
                    transaction.setCustomAnimations(0,0);
                    transaction.commit();

                    AppCompatActivity activity = (AppCompatActivity) requireActivity();
                    if (activity.getSupportActionBar() != null) {
                        activity.getSupportActionBar().setTitle("Map");
                    }
                }
            });
        }
    };
    private FragmentScheduleBinding binding;
    private List<TextView> textViewList = new ArrayList<>();
    // 英文格式日期
    private SimpleDateFormat format = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);

    private int randomColor() {
        return Color.rgb(255, (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(ScheduleViewModel.class);
        binding = FragmentScheduleBinding.bind(view);
        binding.tvMonth.setText(format.format(System.currentTimeMillis()));
        textViewList.add(binding.tvDay1);
        textViewList.add(binding.tvDay2);
        textViewList.add(binding.tvDay3);
        textViewList.add(binding.tvDay4);
        textViewList.add(binding.tvDay5);
        textViewList.add(binding.tvDay6);
        textViewList.add(binding.tvDay7);
        binding.rvData.setAdapter(adapter);
        for (TextView textView : textViewList) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (TextView textView : textViewList) {
                        textView.setBackgroundResource(0);
                    }
                    textView.setBackgroundResource(R.drawable.shape_backgroud);
                    List<String> dataList = new ArrayList<>();
                    int id = v.getId();
                    if (id == R.id.tv_day1) {
                        dataList.addAll(mViewModel.getDataArray(1));
                    } else if (id == R.id.tv_day2) {
                        dataList.addAll(mViewModel.getDataArray(2));
                    } else if (id == R.id.tv_day3) {
                        dataList.addAll(mViewModel.getDataArray(3));
                    } else if (id == R.id.tv_day4) {
                        dataList.addAll(mViewModel.getDataArray(4));
                    } else if (id == R.id.tv_day5) {
                        dataList.addAll(mViewModel.getDataArray(5));
                    } else if (id == R.id.tv_day6) {
                        dataList.addAll(mViewModel.getDataArray(6));
                    } else if (id == R.id.tv_day7) {
                        dataList.addAll(mViewModel.getDataArray(7));
                    }
                    adapter.getData().clear();
                    adapter.getData().addAll(dataList);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(requireActivity(), AddActivity.class), 100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            String content = data.getStringExtra("content");
            int day = data.getIntExtra("day", 1);
            mViewModel.setData(day, content);
            adapter.getData().clear();
            adapter.getData().addAll(mViewModel.getDataArray(day));
            adapter.notifyDataSetChanged();
        }
    }
}
