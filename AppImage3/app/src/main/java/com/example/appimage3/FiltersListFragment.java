package com.example.appimage3;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;

import com.example.appimage3.Adapter.ThumbnailAdapter;
import com.example.appimage3.Interface.FiltersListFragmentListener;
import com.example.appimage3.Utils.BitMapUtils;
import com.example.appimage3.Utils.SpacesItemDecoration;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;

public class FiltersListFragment extends BottomSheetDialogFragment implements FiltersListFragmentListener {

    RecyclerView recyclerView;
    ThumbnailAdapter adapter;
    List<ThumbnailItem> thumbnailItems;

    FiltersListFragmentListener listener;
    static Bitmap bitmap;


    static FiltersListFragment instance;
    public static FiltersListFragment getInstance(Bitmap bitmapsave){
        bitmap=bitmapsave;
        if (instance==null)
        {
            instance=new FiltersListFragment();

        }
        return instance;
    }

    public void setListener(FiltersListFragmentListener listener) {
        this.listener = listener;
    }

    public FiltersListFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView= inflater.inflate(R.layout.fragment_filters_list, container, false);
        thumbnailItems = new ArrayList<>();
        adapter = new ThumbnailAdapter(thumbnailItems, this, getActivity());

        recyclerView =(RecyclerView)itemView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        recyclerView.setAdapter(adapter);

        displayThumbnail(bitmap);

        return itemView;
    }

    protected void displayThumbnail(Bitmap bitmap) {
        Runnable r=new Runnable() {
            @Override
            public void run() {
                Bitmap thumbImg;
                if (bitmap==null){
                    thumbImg= BitMapUtils.getBitmapFromAssets(getActivity(), MainActivity.pictureName,100, 100 );

                }
                else thumbImg = Bitmap.createScaledBitmap(bitmap, 100,100, false);

                if (thumbImg == null) return;

                ThumbnailsManager.clearThumbs();
                thumbnailItems.clear();

                //add normal bitmap first
                ThumbnailItem thumbnailItem=new ThumbnailItem();
                thumbnailItem.image=thumbImg;
                thumbnailItem.filterName="Normal";
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter> filters= FilterPack.getFilterPack(getActivity());

                for (Filter filter:filters){
                    ThumbnailItem tI=new ThumbnailItem();
                    tI.image= thumbImg;
                    tI.filter =filter;
                    tI.filterName=filter.getName();
                    ThumbnailsManager.addThumb(tI);
                }
                thumbnailItems.addAll(ThumbnailsManager.processThumbs(getActivity()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        new Thread(r).start();
    }

    @Override
    public void onFilterSeleted(Filter filter) {
        if (listener!=null){
            listener.onFilterSeleted(filter);
        }
    }
}