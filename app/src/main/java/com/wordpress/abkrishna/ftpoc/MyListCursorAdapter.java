package com.wordpress.abkrishna.ftpoc;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by balakrishna on 12-Dec-16.
 */

public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> {

    Context mContext;
    int mLoaderId;
    int mDataColumnIndex;
    int mTitleColumnIndex;
    int mIdColumnIndex;
    public MyListCursorAdapter(Context context, Cursor cursor, int loaderId){
        super(context,cursor);
        mContext = context;
        mLoaderId = loaderId;
    }
    private void getColumnIndices(Cursor cursor, int loaderId) {
        switch (loaderId) {
            case FileTransferActivityFragment.AUDIO_LOADER_ID:
                mDataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
                mTitleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
                mIdColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                break;
            case FileTransferActivityFragment.VIDEO_LOADER_ID:
                mDataColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                mTitleColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
                mIdColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID);
                break;
            case FileTransferActivityFragment.IMAGE_LOADER_ID:
                mDataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                mTitleColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
                mIdColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                break;
        }
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        if(cursor != null)
            getColumnIndices(cursor, mLoaderId);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mAudioThumb;
        public final CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.filetransfer_name);
            mAudioThumb = (ImageView) view.findViewById(R.id.filetransfer_image);
            mCheckBox = (CheckBox) view.findViewById(R.id.filetransfer_checkbox);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        //viewHolder.mContentView.setText(cursor.getString(mTitleColumnIndex));
        long mediaId = cursor.getInt(mIdColumnIndex);
        String budiName;


        switch (mLoaderId) {
            case FileTransferActivityFragment.IMAGE_LOADER_ID:
                viewHolder.mAudioThumb.setImageBitmap(
                        MediaStore.Images.Thumbnails.getThumbnail(
                                mContext.getContentResolver(),
                                mediaId,
                                MediaStore.Images.Thumbnails.MICRO_KIND,
                                null));
                budiName = cursor.getString(cursor.getColumnIndex("bucket_display_name"));
                Log.v("bucket_display_name", budiName);
                viewHolder.mContentView.setText(budiName);
                break;
            case FileTransferActivityFragment.VIDEO_LOADER_ID:
                viewHolder.mAudioThumb.setImageBitmap(
                        MediaStore.Video.Thumbnails.getThumbnail(
                                mContext.getContentResolver(),
                                mediaId,
                                MediaStore.Images.Thumbnails.MICRO_KIND,
                                null));
                budiName = cursor.getString(cursor.getColumnIndex("bucket_display_name"));
                Log.v("bucket_display_name", budiName);
                viewHolder.mContentView.setText(budiName);
                break;
            case FileTransferActivityFragment.AUDIO_LOADER_ID:
                Drawable imageDrawable = Drawable.createFromPath(cursor.getString(mIdColumnIndex));
                if(imageDrawable != null)
                    viewHolder.mAudioThumb.setImageDrawable(Drawable.createFromPath(cursor.getString(mIdColumnIndex)));
                else
                    viewHolder.mAudioThumb.setImageResource(R.drawable.ic_menu_archive);
                        /*.setImageBitmap(
                        MediaStore.Images.Thumbnails.getThumbnail(
                                mContext.getContentResolver(),
                                mediaId,
                                MediaStore.Images.Thumbnails.MICRO_KIND,
                                null
                        )
                );*/
                String albumName = cursor.getString(cursor.getColumnIndex("album"));
                viewHolder.mContentView.setText(albumName);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_filetransfer, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

}
