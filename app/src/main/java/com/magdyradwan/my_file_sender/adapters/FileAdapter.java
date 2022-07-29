package com.magdyradwan.my_file_sender.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.magdyradwan.my_file_sender.R;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> files;

    public FileAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        files = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.file_item, parent, false);
        }

        TextView fileName = convertView.findViewById(R.id.file_name);

        if(files.get(position).contains("image")) {
            fileName.setText("image" + position);
        }
        else {
            fileName.setText("file" + position);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return files.size();
    }
}
