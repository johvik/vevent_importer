package john.veventimporter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AndroidCalendarListAdapter extends ArrayAdapter<AndroidCalendar> {
    private final LayoutInflater mInflater;

    public AndroidCalendarListAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_item);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<AndroidCalendar> list) {
        clear();
        if (list != null) {
            addAll(list);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView text1;

        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            text1 = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(new ViewHolder(text1));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            text1 = viewHolder.text1;
        }

        AndroidCalendar item = getItem(position);
        text1.setText(item.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView text1;

        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_spinner_dropdown_item,
                    parent, false);
            text1 = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(new ViewHolder(text1));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            text1 = viewHolder.text1;
        }

        AndroidCalendar item = getItem(position);
        text1.setText(item.getName());

        return convertView;
    }

    private class ViewHolder {
        public final TextView text1;

        public ViewHolder(TextView text1) {
            this.text1 = text1;
        }
    }
}
