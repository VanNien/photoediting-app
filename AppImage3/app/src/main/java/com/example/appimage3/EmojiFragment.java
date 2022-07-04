package com.example.appimage3;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appimage3.Adapter.EmojiAdapter;
import com.example.appimage3.Interface.EmojiFragmentListener;

import ja.burhanrashid52.photoeditor.PhotoEditor;


public class EmojiFragment extends BottomSheetDialogFragment implements EmojiAdapter.EmojiAdapterListener {

    RecyclerView recyclerView_emoji;
    static EmojiFragment instance;
    EmojiFragmentListener listener;

    public void setListener(EmojiFragmentListener listener) {
        this.listener = listener;
    }

    public static EmojiFragment getInstance(){
        if (instance==null) instance=new EmojiFragment();
        return instance;
    }

    public EmojiFragment() {
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
        View itemView= inflater.inflate(R.layout.fragment_emoji, container, false);

        recyclerView_emoji=(RecyclerView)itemView.findViewById(R.id.recycler_view_emoji);
        recyclerView_emoji.setHasFixedSize(true);
        recyclerView_emoji.setLayoutManager(new GridLayoutManager(getActivity(),5));

        EmojiAdapter adapter=new EmojiAdapter(getContext(), PhotoEditor.getEmojis(getContext()),this);
        recyclerView_emoji.setAdapter(adapter);

        return itemView;
    }

    @Override
    public void onEmojiItemSelected(String emoji) {
            listener.onEmojiSelected(emoji);
    }
}