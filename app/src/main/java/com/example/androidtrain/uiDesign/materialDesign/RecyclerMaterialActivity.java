package com.example.androidtrain.uiDesign.materialDesign;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidtrain.R;

public class RecyclerMaterialActivity extends Activity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_material);

        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.material_recycler_view);

        //如果内容的改变不需要改变布局的大小，则设置此属性。
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[] myDataSet = new String[20];
        getStringData(myDataSet);

        mAdapter = new MyRecyclerAdapter(myDataSet);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getStringData(String[] myDataSet) {
        for (int i = 0; i < myDataSet.length; i++){
            myDataSet[i] = "RecyclerItem " + i;
        }
    }

    private static class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{

        String[] mData;

        private static final int VIEW_TYPE_ONE = 0;
        private static final int VIEW_TYPE_TWO = 1;



        public MyRecyclerAdapter(String[] data){
            mData = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView mTextView;
            public ViewHolder(View itemView) {
                super(itemView);

                mTextView = (TextView) itemView.findViewById(R.id.material_cardview_textview);
            }
        }

        /**
         * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
         * an item.
         * <p>
         * This new ViewHolder should be constructed with a new View that can represent the items
         * of the given type. You can either create a new View manually or inflate it from an XML
         * layout file.
         * <p>
         * The new ViewHolder will be used to display items of the adapter using
         * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
         * different items in the data set, it is a good idea to cache references to sub views of
         * the View to avoid unnecessary {@link View#findViewById(int)} calls.
         *
         * @param parent   The ViewGroup into which the new View will be added after it is bound to
         *                 an adapter position.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder that holds a View of the given view type.
         * @see #getItemViewType(int)
         * @see #onBindViewHolder(ViewHolder, int)
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder mViewHolder = null;
            View view = null;
            if (viewType == VIEW_TYPE_ONE){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_recyclerview_cardview, parent, false);
            }else if (viewType == VIEW_TYPE_TWO){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_recyclerview_cardview, parent, false);
            }
            mViewHolder = new ViewHolder(view);
            return mViewHolder;
        }

        /**
         * Called by RecyclerView to display the data at the specified position. This method should
         * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
         * position.
         * <p>
         * Note that unlike {@link ListView}, RecyclerView will not call this method
         * again if the position of the item changes in the data set unless the item itself is
         * invalidated or the new position cannot be determined. For this reason, you should only
         * use the <code>position</code> parameter while acquiring the related data item inside
         * this method and should not keep a copy of it. If you need the position of an item later
         * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
         * have the updated adapter position.
         * <p>
         * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
         * handle efficient partial bind.
         *
         * @param holder   The ViewHolder which should be updated to represent the contents of the
         *                 item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int type = holder.getItemViewType();
            if (type == VIEW_TYPE_ONE)
                holder.mTextView.setText(mData[position] + "A");
            else if (type == VIEW_TYPE_TWO)
                holder.mTextView.setText(mData[position] + "B");
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount() {
            return mData.length;
        }

        /**
         * Return the view type of the item at <code>position</code> for the purposes
         * of view recycling.
         * <p>
         * <p>The default implementation of this method returns 0, making the assumption of
         * a single view type for the adapter. Unlike ListView adapters, types need not
         * be contiguous. Consider using id resources to uniquely identify item view types.
         *
         * @param position position to query
         * @return integer value identifying the type of the view needed to represent the item at
         * <code>position</code>. Type codes need not be contiguous.
         */
        @Override
        public int getItemViewType(int position) {
            if (position < 3){
                return VIEW_TYPE_ONE;
            }else{
                return VIEW_TYPE_TWO;
            }
        }
    }
}
