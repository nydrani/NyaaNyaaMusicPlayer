package xyz.lostalishar.nyaanyaamusicplayer.ui.fragment;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xyz.lostalishar.nyaanyaamusicplayer.BuildConfig;
import xyz.lostalishar.nyaanyaamusicplayer.R;
import xyz.lostalishar.nyaanyaamusicplayer.adapter.MusicAdapter;
import xyz.lostalishar.nyaanyaamusicplayer.loader.MusicListLoader;
import xyz.lostalishar.nyaanyaamusicplayer.model.Music;
import xyz.lostalishar.nyaanyaamusicplayer.util.MusicUtils;

/**
 * Fragment containing entire list of music on device
 */

public class MusicListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<Music>> {
    private static final String TAG = MusicListFragment.class.getSimpleName();

    private TextView emptyView;

    public MusicAdapter adapter;

    public static MusicListFragment newInstance() {
        if (BuildConfig.DEBUG) Log.d(TAG, "newInstance");

        return new MusicListFragment();
    }


    //=========================================================================
    // Fragment lifecycle
    //=========================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        adapter = new MusicAdapter(new ArrayList<Music>(), cabHolder);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                emptyView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreateView");

        Activity activity = getActivity();
        RecyclerView.LayoutManager layout = new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activity,
                DividerItemDecoration.VERTICAL);

        View rootView = inflater.inflate(R.layout.list_base, container, false);
        emptyView = rootView.findViewById(R.id.empty_view);
        RecyclerView recyclerView = rootView.findViewById(R.id.list_base_view);

        emptyView.setText(R.string.no_music_found);

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layout);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);
    }


    //=========================================================================
    // Options menu callbacks
    //=========================================================================

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreateOptionsMenu");

        inflater.inflate(R.menu.music_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onOptionsItemSelected");

        int id = item.getItemId();

        switch (id) {
            case R.id.actionbar_refresh:
                refreshList();
                return true;
            case R.id.actionbar_add_all:
                addAll();
                return true;
            default:
                if (BuildConfig.DEBUG) Log.w(TAG, "Unknown menu item id: " + id);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //=========================================================================
    // LoaderManager.LoaderCallbacks
    //=========================================================================

    @Override
    public Loader<List<Music>> onCreateLoader(int id, Bundle args) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreateLoader");

        Activity activity = getActivity();

        return new MusicListLoader(activity);
    }

    @Override
    public void onLoadFinished(Loader<List<Music>> loader, List<Music> data) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onLoadFinished");

        cabHolder.closeCab();
        adapter.swap(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Music>> loader) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onLoadReset");

        cabHolder.closeCab();
        adapter.swap(null);
    }


    //=========================================================================
    // Helper functions
    //=========================================================================

    private void refreshList() {
        if (BuildConfig.DEBUG) Log.d(TAG, "refreshList");

        getLoaderManager().restartLoader(0, null, this);
    }

    private void addAll() {
        if (BuildConfig.DEBUG) Log.d(TAG, "addAll");

        List<Music> musicList = adapter.getMusicList();
        int musicListSize = musicList.size();
        if (BuildConfig.DEBUG) Log.d(TAG, "Music list size: " + musicListSize);

        long[] musicIdArray = new long[musicListSize];
        for (int i = 0; i < musicListSize; i++) {
            musicIdArray[i] = musicList.get(i).getId();
        }

        int numAdded = MusicUtils.enqueue(musicIdArray, null);
        if (BuildConfig.DEBUG) Log.d(TAG, "Number enqueued: " + numAdded);

        String toastFormat = getResources().getString(R.string.toast_add_x_tracks);
        String toastMessage = String.format(toastFormat, numAdded);
        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }
}
