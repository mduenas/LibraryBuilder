package com.markduenas.librarybuilder.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.markduenas.librarybuilder.R;
import com.markduenas.librarybuilder.db.Book;

/**
 * Created by markduenas on 10/30/15.
 */
public class ItemListAdapter extends ArrayAdapter<Book> {

    Context context;
    int layoutResourceId;
    Book data[] = null;

    public ItemListAdapter(Context context, int layoutResourceId, Book[] data) {

        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemListAdapter.ItemHolder();
            holder.imageThumb = (ImageView)row.findViewById(R.id.imageViewThumbnail);
            holder.tvTitle = (TextView)row.findViewById(R.id.tvTitle);
            holder.tvAuthor = (TextView)row.findViewById(R.id.tvAuthor);
            holder.tvPublisher = (TextView)row.findViewById(R.id.tvPublisher);

            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }

        Book book = data[position];
        if (book != null) {
            byte[] bytes = book.thumbNail;
            if (bytes != null && bytes.length > 0) {
                holder.imageThumb.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            } else {
                holder.imageThumb.setImageDrawable(context.getDrawable(R.drawable.no_image));
            }
            holder.tvTitle.setText(book.title);
            holder.tvAuthor.setText(book.author);
            holder.tvPublisher.setText(book.publisher);
        }

        return row;
    }

    static class ItemHolder
    {
        ImageView imageThumb;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvPublisher;
    }
}