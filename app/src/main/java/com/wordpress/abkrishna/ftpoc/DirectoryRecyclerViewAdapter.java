package com.wordpress.abkrishna.ftpoc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.wordpress.abkrishna.ftpoc.data.FileItem;

import java.util.List;

/**
 * Created by balakrishna on 05-Dec-16.
 */

class DirectoryRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<FileItem> mValues;

    static FileTransferActivityFragment.OnDirectoryClickedListener mDirectoryClickedListener;

    DirectoryRecyclerViewAdapter(List<FileItem> items, FileTransferActivityFragment.OnDirectoryClickedListener directoryClickedListener) {
        mValues = items;
        mDirectoryClickedListener = directoryClickedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_filetransfer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(holder.mItem.fileName.equals("0")? "Internal Storage":holder.mItem.fileName);
        int start = (mValues.get(position).isDirectory) ? R.drawable.ic_folder : R.drawable.ic_file;
        holder.imageView.setImageResource(start);
        //holder.mIdView.setCompoundDrawablesWithIntrinsicBounds(start, 0, 0, 0);
        final String filePath = holder.mItem.filePath;
        final FileItem fileItem = holder.mItem;
        holder.mIdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDirectoryClickedListener.onDirectoryClicked(fileItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    //private final View mView;
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
