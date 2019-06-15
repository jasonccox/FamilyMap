package com.jasoncarloscox.familymap.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.FilterItem;

import java.util.List;

/**
 * Adapter to display FilterItems in the RecyclerView on the FilterActivity
 */
public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.ViewHolder> {

    private List<FilterItem> items;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView txtTitle;
        private TextView txtDescription;
        private Switch switchFilter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;

            txtTitle = itemView.findViewById(R.id.filter_item_title);
            txtDescription = itemView.findViewById(R.id.filter_item_description);
            switchFilter = itemView.findViewById(R.id.filter_item_switch);
        }

        public void bindData(final FilterItem item, boolean altBackgound) {
            txtTitle.setText(item.getTitle());

            int descriptionRes = item.isInitiallyFiltered() ? R.string.filter_description_show :
                                                    R.string.filter_description_hide;
            txtDescription.setText(itemView.getResources()
                    .getString(descriptionRes, item.getDescription()));

            switchFilter.setChecked(item.isInitiallyFiltered());
            switchFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // modify the description text
                    int descriptionRes = isChecked ? R.string.filter_description_show :
                                                     R.string.filter_description_hide;
                    txtDescription.setText(itemView.getResources()
                            .getString(descriptionRes, item.getDescription()));

                    // perform actions required by item
                    if (item.getListener() != null) {
                        item.getListener().onFilterStatusChange(isChecked);
                    }
                }
            });

            if (altBackgound) {
                itemView.setBackgroundResource(R.color.colorBackgroundDark);
            }
        }
    }

    public FilterListAdapter(List<FilterItem> items) {
        this.items = items;
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
        // use an alternate background on every other item
        boolean altBackground = (i % 2 == 1);

        viewHolder.bindData(items.get(i), altBackground);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.itemview_filter;
    }
}
