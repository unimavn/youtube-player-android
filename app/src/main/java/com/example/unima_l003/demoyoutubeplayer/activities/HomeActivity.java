package com.example.unima_l003.demoyoutubeplayer.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.unima_l003.demoyoutubeplayer.R;
import com.example.unima_l003.demoyoutubeplayer.adapters.SearchVideoAdapter;
import com.example.unima_l003.demoyoutubeplayer.adapters.VideoAdapter;
import com.example.unima_l003.demoyoutubeplayer.fragments.PlayVideoFragment;
import com.example.unima_l003.demoyoutubeplayer.interfaces.OnItemClickListener;
import com.example.unima_l003.demoyoutubeplayer.managers.SearchManager;
import com.example.unima_l003.demoyoutubeplayer.managers.ThreadManager;
import com.example.unima_l003.demoyoutubeplayer.objects.Video;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private View viewNoConnection;
    private View viewEmpty;
    private SwipeRefreshLayout refreshLayout;
    private List<Video> items;
    private SearchManager searchManager;
    private boolean loading, finished;
    private VideoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        items = new ArrayList<>();
        adapter = new SearchVideoAdapter(items);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder) {
                int position = holder.getLayoutPosition();
                PlayVideoFragment.show(HomeActivity.this, items.get(adapter.getItemVideoPosition(position)));
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstVisibleItem, visibleItemCount, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (loading || finished) return;
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (totalItemCount - visibleItemCount <= firstVisibleItem) {
                    getVideos();
                }
            }

        });

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setEnabled(false);
        viewNoConnection = findViewById(R.id.frame_search_no_connection);
        viewEmpty = findViewById(R.id.tv_search_empty_shultle);

        findViewById(R.id.bt_search_try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideConnection();
            }
        });

        showHideConnection();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.player_main, menu);
        initSearchView(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSearchView(Menu menu) {
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getString(R.string.search_online));
        searchView.setIconifiedByDefault(false);

        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    protected void search(@NonNull String keyword) {
        items.clear();
        adapter.notifyDataSetChanged();
        finished = false;
        searchManager = new SearchManager(keyword, new SearchManager.Callback() {
            @Override
            public void onUpdateResult(final List<Video> videos, final boolean finished, boolean success) {
                loading = false;
                HomeActivity.this.finished = finished;
                ThreadManager.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        if (videos != null) {
                            items.addAll(videos);
                            adapter.notifyDataSetChanged();

                        }

                        if (finished) {
                            if (items.size() == 0) {
                                showEmptySearch();
                            }
                        }

                    }
                });
            }
        });
        getVideos();
    }


    private void getVideos() {
        if (!checkConnection()) {
            showErrorConnection();
            return;
        }
        showSearch();
        loading = true;
        refreshLayout.setRefreshing(true);
        searchManager.search();
    }


    private void showEmptySearch() {
        viewEmpty.setVisibility(View.VISIBLE);
        viewNoConnection.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
    }

    private boolean checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        return activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);

    }

    private void showErrorConnection() {
        viewEmpty.setVisibility(View.GONE);
        viewNoConnection.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
    }

    private void showSearch() {
        viewEmpty.setVisibility(View.GONE);
        viewNoConnection.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.VISIBLE);
    }

    private void showHideConnection() {
        if (!checkConnection()) {
            showErrorConnection();
        } else {
            showSearch();
        }
    }
}
