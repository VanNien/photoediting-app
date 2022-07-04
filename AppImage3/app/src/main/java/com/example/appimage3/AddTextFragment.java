package com.example.appimage3;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.appimage3.Adapter.ColorAdapter;
import com.example.appimage3.Interface.AddTextFragmentListener;
import com.example.appimage3.Interface.BrushFragmentListener;


public class AddTextFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener {

    //color default
    int colorSelected= Color.parseColor("#000000");

    AddTextFragmentListener listener;

    EditText edit_AddText;
    RecyclerView recyclerView_color;
    Button btn_done;

    static AddTextFragment instance;
    public static AddTextFragment getInstance(){
        if (instance==null) instance=new AddTextFragment();
        return instance;
    }

    public void setListener(AddTextFragmentListener listener) {
        this.listener = listener;
    }

    public AddTextFragment() {
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
        View itemView= inflater.inflate(R.layout.fragment_add_text, container, false);

        edit_AddText=(EditText)itemView.findViewById(R.id.edit_add_text);
        btn_done=(Button)itemView.findViewById(R.id.btn_add_text);
        recyclerView_color=(RecyclerView)itemView.findViewById(R.id.recycler_color);
        recyclerView_color.setHasFixedSize(true);
        recyclerView_color.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        ColorAdapter colorAdapter=new ColorAdapter(getContext(),this);
        recyclerView_color.setAdapter(colorAdapter);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddTextButtonClick(edit_AddText.getText().toString(),colorSelected);
            }
        });

        return itemView;
    }

    @Override
    public void onColorSelected(int color) {
        colorSelected=color; //set color select
    }
}