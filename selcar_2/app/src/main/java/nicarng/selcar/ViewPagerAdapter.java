package nicarng.selcar;

import android.content.Context;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;



import nicarng.selcar.Fragment.AllFragment;
import nicarng.selcar.Fragment.UserFragment;
import nicarng.selcar.Fragment.AuctionFragment;
import nicarng.selcar.Fragment.QnAFragment;
import nicarng.selcar.Fragment.NoticeFragment;



public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount=5;

    private AllFragment allFragment = null;


    public ViewPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                allFragment =new AllFragment();
                return allFragment;

            case 1:
                UserFragment userFragment = new UserFragment();
                return userFragment;

            case 2:
                AuctionFragment auctionFragment = new AuctionFragment();
                return auctionFragment;

            case 3:
                QnAFragment qnAFragment= new QnAFragment();
                return qnAFragment;

            case 4:
                NoticeFragment noticeFragment = new NoticeFragment();
                return noticeFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}