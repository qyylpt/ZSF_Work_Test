package com.zsf.test.branch.loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author zsf; 2019/7/31
 */
public class CursorLoaderListFragment extends ListFragment implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * 用于显示列表数据的adapter
     */
    SimpleCursorAdapter simpleCursorAdapter;

    /**
     *  当前搜索过滤器
     */

    String mCurFilter;

    AtomicBoolean show = new AtomicBoolean(true);

    Toast toast;
    View view;
    /**
     * 这是我们想获取的联系人中一行的数据．
     */
    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.CONTACT_STATUS,
            ContactsContract.Contacts.CONTACT_PRESENCE,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.Contacts.LOOKUP_KEY,
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toast = Toast.makeText(getActivity(), "默认信息", Toast.LENGTH_SHORT);
        view = toast.getView();
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {

            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                show.set(true);
            }
        });


        Log.e("zsf", "onActivityCreated");
        // 显示一个空的预制信息
        setEmptyText("当前没有电话号码");

        // 设置菜单栏
        setHasOptionsMenu(true);

        // 设置数据显示适配器，数据为空
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Log.e("zsf", "onActivityCreated" + " 当前版本：" + Build.VERSION.SDK_INT);
            simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, null,
                    new String[] {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.CONTACT_STATUS },
                    new int[] { android.R.id.text1, android.R.id.text2 }, 0 );
        }
        setListAdapter(simpleCursorAdapter);
        // 准备一个loader, 新创建or已经存在的
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(getActivity());
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        if (!show.get()){
            return;
        }
        show.set(false);
        toast.setText("id");
        toast.show();

    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        Log.e("zsf", "onCreateLoader");
        // Loader被创建调用。本例只有一个Loader，所以不需要做Loader的区分
        Uri baseUri;
        if (mCurFilter != null) {
            baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(mCurFilter));
        } else {
            baseUri = ContactsContract.Contacts.CONTENT_URI;
        }

        // 现在创建并返回一个CursorLoader，它将负责创建一个
        // Cursor用于显示数据
        String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
        return new CursorLoader(getActivity(), baseUri,
                CONTACTS_SUMMARY_PROJECTION, select, null,
                ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        // 将新的cursor换进来．(框架将在我们返回时关心一下旧cursor的关闭)
        simpleCursorAdapter.swapCursor(cursor);
        Log.e("zsf", "onLoadFinished");
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        //在最后一个Cursor准备进入上面的onLoadFinished()之前．
        // Cursor要被关闭了，我们需要确保不再使用它．
        simpleCursorAdapter.swapCursor(null);
        Log.e("zsf", "onLoaderReset");
    }

    private boolean isShow = false;
    @Override
    public void onResume() {
        super.onResume();
        isShow = true;
    }

    public void  show(FragmentManager fragmentManager, int id) {
        Log.e("zsf", "------1");
        if (isShow){
            return;
        }
        Log.e("zsf", "------2");
        FragmentTransaction mFt = fragmentManager.beginTransaction();
        if (id == 0) {
            Log.e("zsf", "------3");
            mFt.replace(android.R.id.content, this, getClass().getSimpleName());
        } else {
            Log.e("zsf", "------4");
            mFt.replace(id, this, getClass().getSimpleName());
        }
        mFt.commitAllowingStateLoss();
    }

}
