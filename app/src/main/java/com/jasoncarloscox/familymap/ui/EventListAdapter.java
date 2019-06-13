package com.jasoncarloscox.familymap.ui;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Event;
import com.jasoncarloscox.familymap.util.FAIconGenerator;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<Event> events;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static final int ICON_SIZE_DP = 20;

        private View itemView;
        private ImageView imgIcon;
        private TextView txtTitle;
        private TextView txtSubtitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;

            imgIcon = itemView.findViewById(R.id.person_item_icon);
            txtTitle = itemView.findViewById(R.id.person_item_title);
            txtSubtitle = itemView.findViewById(R.id.person_item_subtitle);
        }

        public void bindData(Event event) {
            Drawable icon = FAIconGenerator.eventIcon(itemView.getContext(),
                                                      ICON_SIZE_DP,
                                                      R.color.colorAccent);
            imgIcon.setImageDrawable(icon);

            txtTitle.setText(event.getType());

            String dateLocation = itemView.getResources()
                    .getString(R.string.event_date_and_location,
                              event.getYear(),
                              event.getCity(),
                              event.getCountry());

            txtSubtitle.setText(dateLocation);
        }
    }

    public EventListAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindData(events.get(i));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.itemview_person_details;
    }
}