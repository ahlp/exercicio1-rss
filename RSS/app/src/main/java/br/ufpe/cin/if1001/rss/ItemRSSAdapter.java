package br.ufpe.cin.if1001.rss;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

// class to adapt ItemRSS list on itemlista
public class ItemRSSAdapter extends ArrayAdapter<ItemRSS> {

    public ItemRSSAdapter(Context context, List<ItemRSS> feed) {
        super(context, 0, feed);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemRSS item = getItem(position);
        // inflate layout with itemlist if null
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemlista, parent, false);
        }

        // setting title and date on respectives textviews
        TextView title = (TextView) convertView.findViewById(R.id.item_titulo);
        title.setText(item.getTitle());
        TextView date = (TextView) convertView.findViewById(R.id.item_data);
        date.setText(item.getPubDate());

        // backgroud white and text white is not good...
        title.setTextColor(Color.BLACK);
        date.setTextColor(Color.BLACK);

        return convertView;
    }
}
