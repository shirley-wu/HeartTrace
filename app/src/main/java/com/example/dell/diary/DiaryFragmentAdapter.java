package com.example.dell.diary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.dell.db.Diary;

import java.util.List;

/**
 * Created by dell on 2018/7/1.
 */

public class DiaryFragmentAdapter extends FragmentStatePagerAdapter {
    List<Fragment> list;

    public DiaryFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public DiaryFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list=list;
    }//写构造方法，方便赋值调用
    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }//根据Item的位置返回对应位置的Fragment，绑定item和Fragment

    @Override
    public int getCount() {
        return list.size();
    }//设置Item的数量

}
