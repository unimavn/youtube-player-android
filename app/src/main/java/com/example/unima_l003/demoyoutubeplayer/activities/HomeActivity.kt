package com.example.unima_l003.demoyoutubeplayer.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.unima_l003.demoyoutubeplayer.R
import com.example.unima_l003.demoyoutubeplayer.adapters.SearchVideoAdapter
import com.example.unima_l003.demoyoutubeplayer.adapters.VideoAdapter
import com.example.unima_l003.demoyoutubeplayer.fragments.PlayVideoFragment
import com.example.unima_l003.demoyoutubeplayer.interfaces.OnItemClickListener
import com.example.unima_l003.demoyoutubeplayer.managers.SearchManager
import com.example.unima_l003.demoyoutubeplayer.managers.ThreadManager
import com.example.unima_l003.demoyoutubeplayer.objects.Video
import java.util.*

class HomeActivity : AppCompatActivity() {

    private var viewNoConnection: View? = null
    private var viewEmpty: View? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private var items: MutableList<Video>? = null
    private var searchManager: SearchManager? = null
    private var loading: Boolean = false
    private var finished: Boolean = false
    private var adapter: VideoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        items = ArrayList()
        adapter = SearchVideoAdapter(items!!)

        adapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(holder: RecyclerView.ViewHolder) {
                val position = holder.layoutPosition
                PlayVideoFragment.show(this@HomeActivity, items!![adapter!!.getItemVideoPosition(position)])
            }
        })
        val recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            internal var firstVisibleItem: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (loading || finished) return
                visibleItemCount = recyclerView!!.childCount
                totalItemCount = layoutManager.itemCount
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (totalItemCount - visibleItemCount <= firstVisibleItem) {
                    getVideos()
                }
            }

        })

        refreshLayout = findViewById(R.id.refresh_layout) as SwipeRefreshLayout
        refreshLayout!!.isEnabled = false
        viewNoConnection = findViewById(R.id.frame_search_no_connection)
        viewEmpty = findViewById(R.id.tv_search_empty_shultle)

        findViewById(R.id.bt_search_try_again).setOnClickListener { showHideConnection() }

        showHideConnection()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.player_main, menu)
        initSearchView(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSearchView(menu: Menu) {
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.queryHint = getString(R.string.search_online)
        searchView.setIconifiedByDefault(false)

        searchView.clearFocus()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })
    }

    protected fun search(keyword: String) {
        items!!.clear()
        adapter!!.notifyDataSetChanged()
        finished = false
        searchManager = SearchManager(keyword, object : SearchManager.Callback {
            override fun onUpdateResult(videos: List<Video>?, finished: Boolean, success: Boolean) {
                loading = false
                this@HomeActivity.finished = finished
                ThreadManager.runInMainThread {
                    refreshLayout!!.isRefreshing = false
                    if (videos != null) {
                        items!!.addAll(videos)
                        adapter!!.notifyDataSetChanged()

                    }

                    if (finished) {
                        if (items!!.size == 0) {
                            showEmptySearch()
                        }
                    }
                }
            }
        })
        getVideos()
    }


    private fun getVideos() {
        if (!checkConnection()) {
            showErrorConnection()
            return
        }
        showSearch()
        loading = true
        refreshLayout!!.isRefreshing = true
        searchManager!!.search()
    }


    private fun showEmptySearch() {
        viewEmpty!!.visibility = View.VISIBLE
        viewNoConnection!!.visibility = View.GONE
        refreshLayout!!.visibility = View.GONE
    }

    private fun checkConnection(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connMgr.activeNetworkInfo
        return activeNetwork != null && (activeNetwork.type == ConnectivityManager.TYPE_WIFI || activeNetwork.type == ConnectivityManager.TYPE_MOBILE)

    }

    private fun showErrorConnection() {
        viewEmpty!!.visibility = View.GONE
        viewNoConnection!!.visibility = View.VISIBLE
        refreshLayout!!.visibility = View.GONE
    }

    private fun showSearch() {
        viewEmpty!!.visibility = View.GONE
        viewNoConnection!!.visibility = View.GONE
        refreshLayout!!.visibility = View.VISIBLE
    }

    private fun showHideConnection() {
        if (!checkConnection()) {
            showErrorConnection()
        } else {
            showSearch()
        }
    }
}
