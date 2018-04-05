package br.ufpe.cin.if1001.rss;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

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
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemlista, null);
        }

        // setting title and date on respectives textviews
        TextView title = (TextView) convertView.findViewById(R.id.item_titulo);
        String titleLink = "<a href=\"" + item.getLink() + "\" >" + item.getTitle() + "</a>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            title.setMovementMethod(LinkMovementMethod.getInstance());
            title.setText(Html.fromHtml(titleLink, Html.FROM_HTML_MODE_LEGACY));
        }
        else {
            title.setMovementMethod(LinkMovementMethod.getInstance());
            title.setText(Html.fromHtml(titleLink));
        }

        TextView date = (TextView) convertView.findViewById(R.id.item_data);
        date.setText(item.getPubDate());

        // backgroud white and text white is not good...
        title.setTextColor(Color.BLACK);
        date.setTextColor(Color.BLACK);

        return convertView;
    }
}
