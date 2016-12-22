package com.wordpress.abkrishna.ftpoc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.wordpress.abkrishna.ftpoc.data.FileItem;

import java.io.File;
import java.util.List;

/**
 * Created by balakrishna on 05-Dec-16.
 */

class DirectoryRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<FileItem> mValues;

    static FileTransferActivityFragment.OnDirectoryClickedListener mDirectoryClickedListener;
    Activity mActivity;

    DirectoryRecyclerViewAdapter(List<FileItem> items,
                                 FileTransferActivityFragment.OnDirectoryClickedListener directoryClickedListener,
                                 Activity activity) {
        mValues = items;
        mDirectoryClickedListener = directoryClickedListener;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_filetransfer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(holder.mItem.fileName.equals("0")? "Internal Storage":holder.mItem.fileName);
        boolean isDirectory = mValues.get(position).isDirectory;
        int start = isDirectory ? R.drawable.ic_folder : R.drawable.ic_file;
        holder.imageView.setImageResource(start);
        final FileItem fileItem = holder.mItem;
        final String filePath = holder.mItem.filePath;
        File fileP = Environment.getExternalStoragePublicDirectory(filePath);
        //final Uri uri2 = Uri.fromFile(fileP);
        //final Uri uri = Uri.parse(holder.mItem.filePath);
        if(isDirectory) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FileTransferActivityFragment.depth++;
                    Log.v("depth ---  ", " " + FileTransferActivityFragment.depth);
                    mDirectoryClickedListener.onDirectoryClicked(fileItem);
                }
            });
            holder.mIdView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FileTransferActivityFragment.depth++;
                    Log.v("depth ---  ", " " + FileTransferActivityFragment.depth);
                    mDirectoryClickedListener.onDirectoryClicked(fileItem);
                }
            });
        } else {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("depth ---  ", " " + FileTransferActivityFragment.depth);
                    String mimeType = getMimeType(filePath);
                    final Uri uri = FileProvider.getUriForFile(mActivity, mActivity.getApplicationContext().getPackageName() + ".provider", new File(holder.mItem.filePath));

                    showFile(uri, mimeType);
                }
            });
            holder.mIdView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("depth ---  ", " " + FileTransferActivityFragment.depth);
                    String mimeType = getMimeType(filePath);
                    final Uri uri = FileProvider.getUriForFile(mActivity, mActivity.getApplicationContext().getPackageName() + ".provider", new File(holder.mItem.filePath));

                    showFile(uri, mimeType);
                }
            });
        }
    }
    private void showFile(Uri fileUri, String mimeType){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(fileUri, mimeType);
        /*if(mimeType.contains("image"))
            mimeType = "image*//*";*/
        mActivity.startActivity(intent);
    }
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    final ImageView imageView;
    final TextView mIdView;
    final CheckBox isSelected;
    FileItem mItem;

    ViewHolder(View view) {
        super(view);
        //mView = view;
        imageView = (ImageView) view.findViewById(R.id.filetransfer_image);
        mIdView = (TextView) view.findViewById(R.id.filetransfer_name);
        isSelected = (CheckBox) view.findViewById(R.id.filetransfer_checkbox);
    }
}
