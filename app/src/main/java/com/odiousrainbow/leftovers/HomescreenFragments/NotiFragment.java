package com.odiousrainbow.leftovers.HomescreenFragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.Adapters.NotificationListAdapter;
import com.odiousrainbow.leftovers.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotiFragment extends Fragment {
    public NotiFragment() {
        // Required empty public constructor
    }

    private RecyclerView notiRecyclerView;
    private ConstraintLayout noNotificationLayout;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private Type type;
    private List<Map<String,String>> stuffsInTula;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noti, container, false);

        notiRecyclerView = view.findViewById(R.id.noti_recycler_view);
        noNotificationLayout = view.findViewById(R.id.no_noti_layout);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        gson = new Gson();
        type = new TypeToken<ArrayList<Map<String,String>>>(){}.getType();
        String json = sharedPreferences.getString(getString(R.string.preference_stored_stuff_key),null);
        if(json != null && !json.equals("[]")){
            stuffsInTula = gson.fromJson(json,type);
            List<Map<String,String>> dataForAdapter = new ArrayList<>();
            for (Map<String,String> m : stuffsInTula){
                if(m.get("iNoti").equals("true")){
                    dataForAdapter.add(m);
                }
            }
            NotificationListAdapter adapter = new NotificationListAdapter(getActivity(),dataForAdapter);
            notiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
            notiRecyclerView.setHasFixedSize(true);
            notiRecyclerView.setAdapter(adapter);
            noNotificationLayout.setVisibility(View.INVISIBLE);
        }
        else{
            noNotificationLayout.setVisibility(View.VISIBLE);
            stuffsInTula = new ArrayList<>();
        }

        return view;
    }

}
